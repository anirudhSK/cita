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
for node in nodevisitor.visit(tree):
        if(isinstance(node,ast.ExprStatement)):
            exprNode=node.expr
            if(isinstance(exprNode,ast.Assign)):  # check if this is an assignment to a Device object
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
                         mobileDeviceList.append(mobileDevice.to_ecma())
          # TODO: Add scoping rules. To make sure nothing is declared globally except for these phone objects. Not sure how to check this yet.
#        if (isinstance(node, ast.FuncCall)) :
#            callgraph[node.identifier.to_ecma()]=[]
#            print node.identifier.to_ecma()
#            for child in node.children() : 
#               if(isinstance(child,ast.FunctionCall)) :
#                  callgraph[node].append(child.identifier.to_ecma());
            #def ecma(x) : print x.to_ecma()
            #map (ecma,node.children())
#print tree.to_ecma() # print awesome javascript :)
# Task 2 : Now make a second pass through the program and look for main() to figure out what needs to be done. 

print mobileDeviceList
