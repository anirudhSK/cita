p=getDeviceByName("Anirudh")
q=getDeviceByName("Lenin")
q=5
function main()
{
    p.watch('driving', "foo");
}
function foo(e)
{
    q.notify("P is driving");
}
