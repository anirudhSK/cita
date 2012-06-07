#! /usr/bin/python
from slimit.visitors import nodevisitor
from slimit import ast
''' List of functions that amount to a pass over the Abstract Syntax tree '''
def FunctionDefinitionsPass(tree) :
     ''' make one pass through the entire tree to get all function definitions.
         TODO: Make the restriction clear that all function declarations and definitions go together in the function foo= { } form 
         TODO: Allow the anonymous function declaration form as well ie f=function(...) { } '''
     fnNames=dict()
     for node in nodevisitor.visit(tree):
        #print type(node)
        if(isinstance(node,ast.FuncDecl)):
            fnNames[node.children()[0].to_ecma()]=node # look up from the function name to the function Declaration object I guess. 
     return fnNames

'''----------------------------------------------------------------'''

def MobileDevicesPass(tree) :
     '''   One pass over the tree to get the list of all mobile devices in the multi script program 
           Parses an expression node in the AST , specifically an expression of the type assignment.
           It returns the string corresponding to the variable name of the mobile device if the assignment
           is indeed getDevice... . Checks for reassignment to mobileDevices using mobileDeviceList '''
     mobileDeviceList=[]
     for node in nodevisitor.visit(tree):
        # Store all mobile nodes in a list ######
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
     return mobileDeviceList
'''----------------------------------------------------------------'''

def PredicatePass(tree) :
     '''   One pass over the tree to get the list of all Predicates in the multi script program 
           Parses an expression node in the AST , specifically an expression of the type assignment.
           It returns the string corresponding to the variable name of the predicate if the assignment
           is indeed a predicate ... . Checks for reassignment of predicates to other variables , which is a type error  '''
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

