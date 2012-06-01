p=getDeviceByName("Anirudh")
q=getDeviceByName("Lenin")
q=getDeviceByName("Arvind")

function main()
{
    p.watch('driving', "foo");
}
function foo(e)
{
    q.notify("P is driving");
}
