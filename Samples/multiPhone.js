p=getDeviceByName("Anirudh")
q=getDeviceByName("Lenin")
r=getDeviceByName("Lenin")
function main()
{
    p.watch('driving', "foo");
}
function foo(e)
{
    q.notify("P is driving");
//    r.notify("P is driving");
}
