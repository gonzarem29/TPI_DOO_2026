import json
mdj = json.load(open('C:/Users/gonza/Documents/DOO-2026/Avances/algo corregido.mdj','r',encoding='utf-8'))
def fb(e,t):
    if isinstance(e,dict):
        if e.get('_id')==t: return e
        for k in ['ownedElements','ownedViews','subViews']:
            if k in e:
                r=fb(e[k],t)
                if r: return r
    elif isinstance(e,list):
        for i in e:
            r=fb(i,t)
            if r: return r
    return None
p=fb(mdj,'AAAAAG17815575555990189000000058')
vs=p.get('ownedViews',[])
edge_types = ('UMLGeneralizationView','UMLAssociationView','UMLInterfaceRealizationView','UMLDependencyView')
bad=0; total=0
for v in vs:
    if v.get('_type') not in edge_types: continue
    total+=1
    h=v.get('head')
    if h is None:
        print('Edge %s: head is None' % v.get('_id','?')[:30])
        bad+=1
    elif not isinstance(h, dict):
        print('Edge %s: head type=%s' % (v.get('_id','?')[:30], type(h).__name__))
        bad+=1
    elif '$ref' not in h:
        print('Edge %s: head has no $ref, keys=%s' % (v.get('_id','?')[:30], list(h.keys())))
        bad+=1
    elif not h['$ref']:
        print('Edge %s: head $ref is empty string' % v.get('_id','?')[:30])
        bad+=1
    else:
        pass  # OK
print('Total edges=%d bad=%d' % (total, bad))
