p=getDeviceByName("Anirudh")
q=getDeviceByName("Lenin")
r=getDeviceByName("Lenin")
pred1=getPredicate("driving","Anirudh")

function main()
{
    p.watch(pred2, "foo");
}
function foo(e)
{
    q.notify("P is driving");
}
