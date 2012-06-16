from slimit.parser import Parser
from slimit.visitors import nodevisitor
from slimit import ast
# my imports
from astPasses import *
import sys
import copy
# One pass to get all the Function Calls that refer to mobile phone objects
# The assumption is that they come before main() or anything else. 
partitionedCode=dict() ; # mapping from string corresponding to object name to string corresponding to the code. 
def AnalyseCallBack(functionDeclaration) :
     ''' Looks at the function body of a call back function and gathers all the phone variables that are accessed.
         This is determined by seeing all expressions with a dot accessor ie x.... ''' 
     assert(isinstance(functionDeclaration,ast.FuncDecl))
     phonesTouched=[]  # to keep track of all the phones that this call back function touched, to decide it's placement. 
     tree=Parser().parse(functionDeclaration.to_ecma());
     for node in nodevisitor.visit(tree):
       if(isinstance(node,ast.DotAccessor)):
          phonesTouched.append(node.children()[0].to_ecma())
     # remove all nested dot accessors , because you don't want q.phone and q.wifi to be counted as phones. Return unique elements of set alone
     phonesTouched = list(set(map (lambda x : x.split('.')[0],phonesTouched)));
     return phonesTouched
def RewriteCallBack(function,predicate,rewrite,predicateList,mobileDeviceList) :
     ''' Rewrite callback function to run on a single phone. As is, it's a multi phone script function.
         It needs to be rewritten into a single phone function '''
     assert(isinstance(function,ast.FuncDecl))
     print predicateList
     cloneFunction=copy.deepcopy(function)
     predicate = predicate.replace('\"','')
     predicate = predicate.replace('\'','')
     cloneFunction.parameters[0]=ast.Identifier(predicate) # Rewrite the argument with something else. # ANIRUDH: Change for the demo. 
     cloneFunction.elements=map(lambda x: RewriteFunctionBody(x,mobileDeviceList) ,cloneFunction.elements) if rewrite else cloneFunction.elements 
     return cloneFunction
def RewriteFunctionBody(functionBody,mobileDeviceList) :
     ''' Called by RewriteCallBack, to rewrite the function body. RewriteCallBack takes care of rewriting
         the function name and signature '''
     if(type(functionBody)==type([])) :             # need to handle lists. 
               for element in functionBody :
                   element=RewriteFunctionBody(element,mobileDeviceList)
               return functionBody
     elif (isinstance(functionBody,ast.DotAccessor)) :
               if (functionBody.children()[0].to_ecma() in mobileDeviceList) :
                 return functionBody.children()[1] # strip off the name of the calling object if and only if it's a mobile device object. q.phone.vibrate -> phone.vibrate and not vibrate. 
               else :                              # Assuming all predicates are well defined, this can happen only if we have a nested accessor. : ANIRUDH 
                 rewrittenObj=RewriteFunctionBody(functionBody.children()[0],mobileDeviceList)
                 return (ast.DotAccessor(rewrittenObj,functionBody.children()[1]))
     elif (len(functionBody.children())==0):
               return functionBody      
     else : 
               for key in functionBody.__dict__ :
                  functionBody.__dict__[key]=RewriteFunctionBody(functionBody.__dict__[key],mobileDeviceList)
               return functionBody
def ParseMethodCalls(exprNode,fnNames,mobileDeviceList,predicateList):
     ''' Parse method calls in the original multi phone script program.
          If you find that it's a watch function, do the appropriate partitioning.
          If you find the function is undeclared, given a list of fnNames, abort and indicate an error
          If you find that a phone object being accessed does not exist in mobileDeviceList, 
          Check against the predicateList as well to make sure that the predicate is indeed declared
          indicate an error again '''
     global partitionedCode
     assert(isinstance(exprNode,ast.FunctionCall))
     fnCallNode=exprNode.children()[0]
     if(isinstance(fnCallNode,ast.DotAccessor)): # check if this is a method call to an object 
         methodName=fnCallNode.children()[1].to_ecma();
         watchMan=fnCallNode.children()[0].to_ecma();
         watchMan=watchMan.strip('.')[0]; # strip off all the nested dot accessors. 
         if(watchMan not in mobileDeviceList) :
             raise Exception("Calling method on non-existent phone ", watchMan," The Code can't literally run in the air !! ")
         if(methodName=="watch"):                # get watchman, predicate and action
           predicate=exprNode.children()[1].to_ecma()
           action=exprNode.children()[2].to_ecma()
           if(predicate not in predicateList) :
             raise Exception("Calling method on non-existent predicate ", predicate," The Code can't run !! ")
   #        print "Detected a watch call on phone ",watchMan," with predicate",predicate," and action ",action
           action = action.replace('\"','')
           action = action.replace('\'','')
           if(action not in fnNames):
              raise Exception("Invalid watch call because the call back function ie ",action," for the action is undefined in ",fnNames)
           else :
  #            print "Function Body of the Action is",fnNames[action].to_ecma()
              touchedNodes=AnalyseCallBack(fnNames[action]);
              callBackNode=touchedNodes[0] if (len(touchedNodes)==1) else "server"
           if (watchMan not in partitionedCode) : partitionedCode[watchMan]=''
           if (callBackNode not in partitionedCode): partitionedCode[callBackNode]=''
           partitionedCode[watchMan]=partitionedCode[watchMan] + "addCallback("+predicateList[predicate]+","+"\""+mobileDeviceList[callBackNode]+":"+action+"\") \n"
           # addCallback because watch isn't defined yet in the CITA run time. 
           rewrittenFunction=RewriteCallBack(fnNames[action],predicate,True,predicateList,mobileDeviceList) if callBackNode != "server"  \
                                                                                                            else RewriteCallBack(fnNames[action],predicate,False,predicateList,mobileDeviceList)
           partitionedCode[callBackNode]=partitionedCode[callBackNode]+rewrittenFunction.to_ecma().replace("\n"," ");

# TODO: Add scoping rules. To make sure nothing is declared globally except for these phone objects. Not sure how to check this yet. Maybe this is ok 

def partitionCode(sourceCode):
  parser = Parser()
  print "Source code originally is ............ \n", sourceCode
  tree=parser.parse(sourceCode);
  fnList=FunctionDefinitionsPass(tree)
  mobileDeviceList=MobileDevicesPass(tree)
  predicateList=PredicatePass(tree)
  for node in nodevisitor.visit(tree):
        if(isinstance(node,ast.ExprStatement)):
            exprNode=node.expr
            if(isinstance(exprNode,ast.FunctionCall)):  # check if this is a function call to an object 
                ParseMethodCalls(exprNode,fnList,mobileDeviceList,predicateList) # TODO: Impose the restiction that all mobile Device declarations come ahead of all else
  print "-*********\n*******\n------------------THE PARTITIONED CODE IS -----------------------------*********\n*******\n"
  returnCode =dict()
  for key in partitionedCode :
        print "On node ",mobileDeviceList[key],", code is \n\n"
        print partitionedCode[key]
        returnCode[mobileDeviceList[key]]=partitionedCode[key]
  return returnCode

if __name__ == "__main__":
  if(len(sys.argv) < 2) : 
    print "Usage : python partitioning.py fileName \n"
    sys.exit(1)
  sourceCode=open(sys.argv[1]).read()
  sys.exit(partitionCode(sourceCode))
