import json
import copy
import sys

with open('C:/Users/gonza/Documents/DOO-2026/Avances/algo corregido.mdj', 'r', encoding='utf-8') as f:
    mdj = json.load(f)

def search(elem, tid):
    if isinstance(elem, dict):
        if elem.get('_id') == tid:
            return elem
        for v in elem.values():
            r = search(v, tid)
            if r:
                return r
    elif isinstance(elem, list):
        for i in elem:
            r = search(i, tid)
            if r:
                return r
    return None

p = search(mdj, 'AAAAAG17815575555990189000000058')
cvs = [v for v in p['ownedViews'] if v.get('_type') == 'UMLClassView']
edges = [v for v in p['ownedViews'] if v.get('_type') in ('UMLAssociationView','UMLGeneralizationView','UMLInterfaceRealizationView','UMLDependencyView')]

# Build class view map by id
cv_map = {}
for cv in cvs:
    vid = cv['_id']
    name = '?'
    for sv in cv.get('subViews',[]):
        for lv in sv.get('subViews',[]):
            if lv.get('_type') == 'LabelView' and 'text' in lv:
                t = lv['text']
                if t and t != '(from Logical View)':
                    name = t
    cv_map[vid] = {'view': cv, 'name': name}
    print(f"  Class: {name:20s}  id={vid}  pos=({cv.get('left')},{cv.get('top')})")

print()
print("=== EDGES ===")
for e in edges:
    et = e.get('_type')
    head_vid = e['head']['$ref']
    tail_vid = e['tail']['$ref']
    sn = cv_map.get(tail_vid, {}).get('name', '?')
    tn = cv_map.get(head_vid, {}).get('name', '?')
    print(f"  {et:25s}  {sn:20s} -> {tn:20s}")
