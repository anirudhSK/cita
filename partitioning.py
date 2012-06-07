from slimit.parser import Parser
from slimit.visitors import nodevisitor
from slimit import ast
import sys
import copy
# One pass to get all the Function Calls that refer to mobile phone objects
# The assumption is that they come before main() or anything else. 
partitionedCode=dict() ; # mapping from string corresponding to object name to string corresponding to the code. 
def GetMobileDevice(exprNode,mobileDeviceList) :
     ''' Parses an expression node in the AST , specifically an expression of the type assignment.
         It returns the string corresponding to the variable name of the mobile device if the assignment
         is indeed getDevice... . Checks for reassignment to mobileDevices using mobileDeviceList '''
     assert(isinstance(exprNode,ast.Assign))
     identifierName=exprNode.children()[0].to_ecma()
     if(identifierName in mobileDeviceList) :
           raise Exception("Re-assignment to variable name ",identifierName," that represents a mobile phone")
           sys.exit(2)
     if(len(exprNode.children())==2) : # Check if the expr has exactly two children , because otherwise it can't be a function call assignment
       if( (isinstance(exprNode.children()[0],ast.Identifier)) and (isinstance(exprNode.children()[1],ast.FunctionCall))) : # check if the LHS is an identifier and the RHS is a fnCall
            fnCallNode=node.expr.children()[1] # get fnCall
            functionName=fnCallNode.children()[0].to_ecma() # getString repr.
            if (functionName.startswith("getDeviceByName")): # maybe add more calls in the future, TODO: Need some analysis to approx. the call results at compile time or defer to runtime
                mobileDevice=exprNode.children()[0]
                return mobileDevice.to_ecma()
def AnalyseCallBack(functionDeclaration) :
     ''' Looks at the function body of a call back function and gathers all the phone variables that are accessed.
         This is determined by seeing all expressions with a dot accessor ie x.... ''' 
     assert(isinstance(functionDeclaration,ast.FuncDecl))
     phonesTouched=[]  # to keep track of all the phones that this call back function touched, to decide it's placement. 
     tree=parser.parse(functionDeclaration.to_ecma());
     for node in nodevisitor.visit(tree):
       if(isinstance(node,ast.DotAccessor)):
          phonesTouched.append(node.children()[0].to_ecma())
     return phonesTouched
def RewriteCallBack(function,predicate,rewrite) :
     ''' Rewrite callback function to run on a single phone. As is, it's a multi phone script function.
         It needs to be rewritten into a single phone function '''
     assert(isinstance(function,ast.FuncDecl))
     cloneFunction=copy.deepcopy(function)
     predicate = predicate.replace('\"','')
     predicate = predicate.replace('\'','')
     cloneFunction.parameters[0]=ast.Identifier(predicate) # Rewrite the argument with something else. 
     cloneFunction.elements=map(RewriteFunctionBody,cloneFunction.elements) if rewrite else cloneFunction.elements 
     return cloneFunction
def RewriteFunctionBody(functionBody) :
     ''' Called by RewriteCallBack, to rewrite the function body. RewriteCallBack takes care of rewriting
         the function name and signature '''
     if(type(functionBody)==type([])) :             # need to handle lists. 
               for element in functionBody :
                   element=RewriteFunctionBody(element)
               return functionBody
     elif (isinstance(functionBody,ast.DotAccessor)) :
               return functionBody.children()[1] # strip off the name of the calling object. 
     elif (len(functionBody.children())==0):
               return functionBody      
     else : 
               for key in functionBody.__dict__ :
                  functionBody.__dict__[key]=RewriteFunctionBody(functionBody.__dict__[key])
               return functionBody
def ParseMethodCalls(exprNode,fnNames,mobileDeviceList):
     ''' Parse method calls in the original multi phone script program.
          If you find that it's a watch function, do the appropriate partitioning.
          If you find the function is undeclared, given a list of fnNames, abort and indicate an error
          If you find that a phone object being accessed does not exist in mobileDeviceList, 
          indicate an error again '''
     global partitionedCode
     assert(isinstance(exprNode,ast.FunctionCall))
     fnCallNode=exprNode.children()[0]
     if(isinstance(fnCallNode,ast.DotAccessor)): # check if this is a method call to an object 
         methodName=fnCallNode.children()[1].to_ecma();
         watchMan=fnCallNode.children()[0].to_ecma();
         if(watchMan not in mobileDeviceList) :
             raise Exception("Calling method on non-existent phone ", watchMan," The Code can't literally run in the air !! ")
         if(methodName=="watch"):                # get watchman, predicate and action
           predicate=exprNode.children()[1].to_ecma()
           action=exprNode.children()[2].to_ecma()
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
           partitionedCode[watchMan]=partitionedCode[watchMan] + "watch("+predicate+","+"\""+callBackNode+":"+action+"\") \n"
           rewrittenFunction=RewriteCallBack(fnNames[action],predicate,True) if callBackNode != "server" else RewriteCallBack(fnNames[action],predicate,False)
           partitionedCode[callBackNode]=partitionedCode[callBackNode]+rewrittenFunction.to_ecma()+"\n";

# TODO: Add scoping rules. To make sure nothing is declared globally except for these phone objects. Not sure how to check this yet. Maybe this is ok 
def GetFunctionDefinitions(tree) :
     ''' make one pass through the entire tree to get all function definitions.
         TODO: Make the restriction clear that all function declarations and definitions go together in the function foo= { } form 
         TODO: Allow the anonymous function declaration form as well ie f=function(...) { } '''
     fnNames=dict()
     for node in nodevisitor.visit(tree):
        #print type(node)
        if(isinstance(node,ast.FuncDecl)):
            fnNames[node.children()[0].to_ecma()]=node # look up from the function name to the function Declaration object I guess. 
     return fnNames

if __name__ == "__main__":
  parser = Parser()
  if(len(sys.argv) < 2) : 
    print "Usage : python partitionin.py fileName \n"
    sys.exit(1)
  sourceCode=open(sys.argv[1]).read()
  print "Source code originally is ............ \n", sourceCode
  tree=parser.parse(sourceCode);
  mobileDeviceList=[]
  fnList=GetFunctionDefinitions(tree)
  for node in nodevisitor.visit(tree):
        # Store all mobile nodes in a list ######
        if(isinstance(node,ast.ExprStatement)):
            exprNode=node.expr
            if(isinstance(exprNode,ast.Assign)):  # check if this is an assignment to a Device object
               mobileDeviceList.append(GetMobileDevice(exprNode),mobileDeviceList)
            if(isinstance(exprNode,ast.FunctionCall)):  # check if this is a function call to an object 
                ParseMethodCalls(exprNode,fnList,mobileDeviceList) # TODO: Impose the restiction that all mobile Device declarations come ahead of all else
  print "-*********\n*******\n------------------THE PARTITIONED CODE IS -----------------------------*********\n*******\n"

  for key in partitionedCode :
        print "On node ",key,", code is \n\n"
        print partitionedCode[key]
