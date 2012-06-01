p=getDeviceByName("Anirudh")
q=getDeviceByName("Lenin")
function main()
{
    p.watch('driving', "fooz");
}
function foo(e)
{
    q.notify("P is driving");
}
