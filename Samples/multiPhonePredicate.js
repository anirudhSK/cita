a=getDevice("Lenin")
p=getPredicate(a,"walking",vararg)
q=getPredicate(b,"driving",vararg)
r=getPredicate(c,"driving",vararg)
s=r.and(q)
z=s.and(getPredicate(c,"walking",vararg))
t=z.and(q).and(s)
p=p.and(p)
p=p.and(q)
p=p.or(q)
p=p.within(q,5)
p=p.fortime(5)
