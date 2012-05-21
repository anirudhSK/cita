from slimit.parser import Parser
from slimit.visitors import nodevisitor
from slimit import ast
import sys
parser = Parser()
#tree = parser.parse('(function(){var g=void 0,h=null,l=!1;function m(){return n.navigator?n.navigator.userAgent:h}function p(d){var a;if(!(a=q[d])){a=0;for(var c=(""+r).replace(/^[\s\xa0]+|[\s\xa0]+$/g,"").split("."),b=(""+d).replace(/^[\s\xa0]+|[\s\xa0]+$/g,"").split("."),f=Math.max(c.length,b.length),e=0;0==a&&e<f;e++){var k=c[e]||"",o=b[e]||"",M=RegExp("(\\d*)(\\D*)","g"),N=RegExp("(\\d*)(\\D*)","g");do{var i=M.exec(k)||["","",""],j=N.exec(o)||["","",""];if(0==i[0].length&&0==j[0].length)break;a=((0==i[1].length?0:parseInt(i[1],10))<(0==j[1].length?0:parseInt(j[1],10))?-1:(0==i[1].length?0:parseInt(i[1],10))>(0==j[1].length?0:parseInt(j[1],10))?1:0)||((0==i[2].length)<(0==j[2].length)?-1:(0==i[2].length)>(0==j[2].length)?1:0)||(i[2]<j[2]?-1:i[2]>j[2]?1:0)}while(0==a)}a=q[d]=0<=a}return a}function s(d,a){var c=d.search(t),b;a:{b=0;for(var f=a.length;0<=(b=d.indexOf(a,b))&&b<c;){var e=d.charCodeAt(b-1);if(38==e||63==e)if(e=d.charCodeAt(b+f),!e||61==e||38==e||35==e)break a;b+=f+1}b=-1}if(0>b)return h;f=d.indexOf("&",b);if(0>f||f>c)f=c;b+=a.length+1;return decodeURIComponent(d.substr(b,f-b).replace(/\+/g," "))}function u(){var d=top.location.pathname,a=top.location.search;return!!top.location.hostname.match(/\.google\.com$/)&&"/mail/aca/"==d&&"?cabg"==a}var n=this,v=Date.now||function(){return+new Date};var w,x,y,z;z=y=x=w=l;var A;if(A=m()){var B=n.navigator;w=0==A.indexOf("Opera");x=!w&&-1!=A.indexOf("MSIE");y=!w&&-1!=A.indexOf("WebKit");z=!w&&!y&&"Gecko"==B.product}var C=w,D=x,E=z,F=y,G;a:{var H="",I;if(C&&n.opera)var J=n.opera.version,H="function"==typeof J?J():J;else if(E?I=/rv\:([^\);]+)(\)|;)/:D?I=/MSIE\s+([^\);]+)(\)|;)/:F&&(I=/WebKit\/(\S+)/),I)var K=I.exec(m()),H=K?K[1]:"";if(D){var L,O=n.document;L=O?O.documentMode:g;if(L>parseFloat(H)){G=""+L;break a}}G=H}var r=G,q={};var t=/#|$/;var P=Array.prototype,Q=P.forEach?function(d,a,c){P.forEach.call(d,a,c)}:function(d,a,c){for(var b=d.length,f="string"==typeof d?d.split(""):d,e=0;e<b;e++)e in f&&a.call(c,f[e],e,d)};var R=[];n._ValidateBrowser=function $a(){var a=window.location.href,c;if(!(c=u()?h:window!=top||window.frameElement!=h?a:h)){if(a=-1==a.indexOf("nocheckbrowser")){var b=function $(a,b){return a!==g?a:b()};if(R.reduce)a=R.reduce(b,g);else{var f=g;Q(R,function(a,c){f=b.call(g,f,a,c,R)});a=f}a=!(a!==g?a:D&&p("7.0")||E&&p("1.5")||F&&p("522")||C&&p("9.5"))}if(!(a=a?n.GM_MOOSE_URL:h))if(document.cookie="jscookietest=valid",-1!=document.cookie.indexOf("jscookietest=valid")?(document.cookie="jscookietest=valid;expires=Thu, 01 Jan 1970 00:00:00 GMT",a=h):a=n.GM_NO_COOKIE_URL,!a){a:{if(D&&!p("10"))try{new ActiveXObject("Msxml2.XMLHTTP")}catch(e){a=n.GM_NO_ACTIVEX_URL;break a}a=h}if(!a)if(!n.GM_CA_ENABLED||!window.chrome||s(top.location.href,"view"))a=h;else if(a=(a=s(top.location.href,"cam"))&&+a,c=!!window.localStorage.getItem("mail.google.com.bgLaunched"),!u()&&(c&&0!=a||1==a)){a=top.location;c=a.pathname.match("/mail/(?:a?ca/)?(.*)");var k="/mail/aca/";c&&2==c.length&&(k+=c[1]);c=a.protocol+"//"+a.host+k;var k=a.search,o="?view=fstf";0<k.length&&(o+="&"+k.substr(1));a=c+o+a.hash}else a=h}c=a}if(a=c)u()?window.location=a:top.location=a};R.push(function(){if(D&&!p("8.0")||E&&!p("1.9.2"))return l});_ValidateBrowser()})();')
#tree=parser.parse('function example(a,b) {number += a;alert("You have chosen: " + b);}');
#tree=parser.parse();
#src=open(sys.argv[1]).read();
#src=('Scheduler.prototype.addTask = function (id, priority, queue, task) { this.currentTcb = new TaskControlBlock(this.list, id, priority, queue, task);  this.list = this.currentTcb;  this.blocks[id] = this.currentTcb;};')
src=('rson = new Object(); person.name = "Tim Scarfe"; person.height = "6Ft"; person.run = function() { 	this.state = "running" ;this.speed = "4ms^-1" }')

#print src
tree=parser.parse(src);

callgraph=dict();
for node in nodevisitor.visit(tree):
        print type(node)
#        print node.to_ecma()
###        if (isinstance(node, ast.FuncExpr)) :  # anonymous or lambda functions. 
###            callgraph[node.identifier.to_ecma()]=[]
###            print node.identifier.to_ecma()
####            print node.to_ecma()
####            print node.children()
###	    def ecma(x) : print x.to_ecma()
####            map (ecma,node.children())
###            for child in node.children() : 
###               if(isinstance(child,ast.FunctionCall)) :
###                  callgraph[node.identifier.to_ecma()].append(child.identifier.to_ecma());
###               if(isinstance(child,ast.ExprStatement)) : # member function calls 
###                  firstGrandChild=child.children()[0]
###		  if(isinstance(firstGrandChild,ast.FunctionCall)) :
###                 	 callgraph[node.identifier.to_ecma()].append(firstGrandChild.identifier.to_ecma());
###
###        if (isinstance(node, ast.FuncDecl)) :
###            callgraph[node.identifier.to_ecma()]=[]
###            print node.identifier.to_ecma()
####            print node.to_ecma()
####            print node.children()
###	    def ecma(x) : print x.to_ecma()
####            map (ecma,node.children())
###            for child in node.children() : 
###               if(isinstance(child,ast.FunctionCall)) :
###                  callgraph[node.identifier.to_ecma()].append(child.identifier.to_ecma());
###               if(isinstance(child,ast.ExprStatement)) : # member function calls 
###                  firstGrandChild=child.children()[0]
###		  if(isinstance(firstGrandChild,ast.FunctionCall)) :
###                 	 callgraph[node.identifier.to_ecma()].append(firstGrandChild.identifier.to_ecma());
###                  #map(ecma,child.children())
#                  callgraph[node].append(child.identifier.to_ecma());
    
#            print "End of function ****** \n\n\n\n"
#print tree.to_ecma() # print awesome javascript :)
print callgraph
