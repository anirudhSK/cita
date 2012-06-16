p=getDeviceByName("anirudh")
q=getDeviceByName("nexus")
pred=getPredicate("shake");
function main() {  p.watch(pred, "foo"); }
function foo(e) {  q.phone.vibrate(); }
