from slimit.parser import Parser
from slimit.visitors import nodevisitor
from slimit import ast
import sys
parser = Parser()
if(len(sys.argv) < 2) : 
   print "Usage : python ast.py "
   sys.exit(1)
sourceCode=open(sys.argv[1]).read()
print "Source code originally is ............ \n", sourceCode
tree=parser.parse(sourceCode);
callgraph=dict();
# One pass to get all the Function Calls that refer to mobile phone objects
# The assumption is that they come before main() or anything else. 
mobileDeviceList=[];

def GetMobileDevice(exprNode) :
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
     assert(isinstance(functionDeclaration,ast.FuncDecl))
     phonesTouched=[]  # to keep track of all the phones that this call back function touched, to decide it's placement. 
     tree=parser.parse(functionDeclaration.to_ecma());
     for node in nodevisitor.visit(tree):
       if(isinstance(node,ast.DotAccessor)):
          phonesTouched.append(node.children()[0].to_ecma())
     return phonesTouched

def ParseMethodCalls(exprNode,fnNames,mobileDeviceList):
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
           print "Detected a watch call on phone ",watchMan," with predicate",predicate," and action ",action
           action = action.replace('\"','')
           action = action.replace('\'','')
           if(action not in fnNames):
              print fnNames
              raise Exception("Invalid watch call because the call back function for the action is undefined ")
           else :
              print "Function Body of the Action is",fnNames[action].to_ecma()
              AnalyseCallBack(fnNames[action]);
# TODO: Add scoping rules. To make sure nothing is declared globally except for these phone objects. Not sure how to check this yet. Maybe this is ok 

def GetFunctionDefinitions(tree) :
     # make one pass through the entire tree to get all function definitions.
     # TODO: Make the restriction clear that all function declarations and definitions go together in the function foo= { } form 
     fnNames=dict()
     for node in nodevisitor.visit(tree):
        #print type(node)
        if(isinstance(node,ast.FuncDecl)):
            fnNames[node.children()[0].to_ecma()]=node # look up from the function name to the function Declaration object I guess. 
     return fnNames

if __name__ == "__main__":
  fnList=GetFunctionDefinitions(tree)
  for node in nodevisitor.visit(tree):
        # Store all mobile nodes in a list ######
        if(isinstance(node,ast.ExprStatement)):
            exprNode=node.expr
            if(isinstance(exprNode,ast.Assign)):  # check if this is an assignment to a Device object
               mobileDeviceList.append(GetMobileDevice(exprNode))
            if(isinstance(exprNode,ast.FunctionCall)):  # check if this is a function call to an object 
                ParseMethodCalls(exprNode,fnList,mobileDeviceList) # TODO: Impose the restiction that all mobile Device declarations come ahead of all else
  print mobileDeviceList
