#! /usr/bin/python
from slimit.visitors import nodevisitor
from slimit import ast
''' At compile time, evaluate the predicates simply statically and then build an AST of these predicates 
    This AST should have sufficeint leaves which tell you what nodes the predicate touches .
    First step : Design an AST for this. Algebraic Data Type in Python :) '''
# There are no ADT's in Python. But I can probably use this or an expression evaluator
def PredicatePass(tree) :
     # scan only global statements, nothing within functions. 
     '''   One pass over the tree to get the list of all Predicates in the multi script program 
           Parses an expression node in the AST , specifically an expression of the type assignment.
           It returns the string corresponding to the variable name of the predicate if the assignment
           is indeed a predicate ... . Checks for reassignment of predicates to other variables , 
           which is a type error  '''
     predicateList=[]
     for node in nodevisitor.visit(tree):
        # Store all mobile nodes in a list ######
        if(isinstance(node,ast.ExprStatement)):
            exprNode=node.expr
            if(isinstance(exprNode,ast.Assign)):  # check if this is an assignment to a Device object
               identifierName=exprNode.children()[0].to_ecma()
               if(identifierName in predicateList) :
                     raise Exception("Re-assignment to variable name ",identifierName," that represents a mobile phone")
                     sys.exit(2)
               if(len(exprNode.children())==2) : # Check if the expr has exactly two children , because otherwise it can't be a function call assignment
                 if( (isinstance(exprNode.children()[0],ast.Identifier)) and (isinstance(exprNode.children()[1],ast.FunctionCall))) : # check if the LHS is an identifier and the RHS is a fnCall
                      fnCallNode=node.expr.children()[1] # get fnCall
                      functionName=fnCallNode.children()[0].to_ecma() # getString repr.
                      if (functionName.startswith("getPredicate")): # maybe add more calls in the future, TODO: Need some analysis to approx. the call results at compile time or defer to runtime
                          predicateVariable=exprNode.children()[0]
                          predicateList.append(predicateVariable.to_ecma())
     return predicateList
if __name__ == "__main__" : 
     parser=Parser() 
     if(len(sys.argv) < 2) : 
       print "Usage : python evaluatePredicates.py fileName \n"
       sys.exit(1)
     sourceCode=open(sys.argv[1]).read()
     print "Source code originally is ............ \n", sourceCode
     tree=parser.parse(sourceCode);
