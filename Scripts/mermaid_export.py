import json

PATH = 'C:/Users/gonza/Documents/DOO-2026/Avances/algo corregido.mdj'

with open(PATH, 'r', encoding='utf-8') as f:
    mdj = json.load(f)

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

def get_model(mdj, ref):
    m = fb(mdj, ref)
    return m

patrones = fb(mdj, 'AAAAAG17815575555990189000000058')
views = patrones.get('ownedViews', [])

# Build class view lookup
class_views = {}
for v in views:
    if v.get('_type') == 'UMLClassView':
        cid = v.get('_id','')
        mid = v.get('model',{}).get('$ref','')
        m = get_model(mdj, mid)
        name = m.get('name','?') if m else '?'
        left = v.get('left',0)
        top = v.get('top',0)
        # Get attributes and operations from model
        attrs = []
        ops = []
        if m:
            for oe in m.get('ownedElements',[]):
                if oe.get('_type') == 'UMLAttribute':
                    attrs.append(oe.get('name','?'))
                elif oe.get('_type') == 'UMLOperation':
                    ops.append(oe.get('name','?'))
        class_views[cid] = {
            'name': name, 'left': left, 'top': top,
            'attrs': attrs, 'ops': ops, 'x_zone': left // 200
        }

# Zone labels
zones = {0:'DAO+Factory', 1:'MVC', 2:'State', 3:'Strategy', 4:'Observer'}
for cid, info in class_views.items():
    zone_num = info['left'] // 200
    info['zone'] = zones.get(zone_num, f'Zone{zone_num}')
    x = info['left']
    if x < 150: info['zone'] = 'DAO+Factory'
    elif x < 400: info['zone'] = 'MVC'
    elif x < 800: info['zone'] = 'State'
    elif x < 1000: info['zone'] = 'Strategy'
    else: info['zone'] = 'Observer'

# Collect edges
lines = []
edge_types = ('UMLGeneralizationView','UMLAssociationView','UMLInterfaceRealizationView','UMLDependencyView')
for v in views:
    if v.get('_type') not in edge_types: continue
    hid = v.get('head',{}).get('$ref','')
    tid = v.get('tail',{}).get('$ref','')
    if hid not in class_views or tid not in class_views: continue
    h = class_views[hid]['name']
    t = class_views[tid]['name']
    vt = v.get('_type','')
    arrow = '--|>'
    label = ''
    if vt == 'UMLDependencyView':
        arrow = '..>'
        label = ' <<dependency>>'
    elif vt == 'UMLAssociationView':
        arrow = '-->'
        label = ''
    lines.append((t, arrow, h, vt))

# Build class sections by zone
zone_order = ['DAO+Factory', 'MVC', 'State', 'Strategy', 'Observer']
print("```mermaid")
print("classDiagram")
print("    classDAO_Factory {")
print("        <<zone>>")
print("    }")
print("    class MVC {")
print("        <<zone>>")
print("    }")
print("    class State {")
print("        <<zone>>")
print("    }")
print("    class Strategy {")
print("        <<zone>>")
print("    }")
print("    class Observer {")
print("        <<zone>>")
print("    }")

# Print all classes with their attributes/ops
for cid, info in sorted(class_views.items(), key=lambda kv: (kv[1]['zone'], kv[1]['top'])):
    n = info['name']
    z = info['zone']
    print(f"    class {n} {{")
    print(f"        <<{z}>>")
    # Skip attributes/ops to keep it clean
    print(f"    }}")

# Print relationships
for t, arrow, h, vt in lines:
    print(f"    {t} {arrow} {h}")

print("```")
print("\n--- POSITIONS (left,top) ---")
for cid, info in sorted(class_views.items(), key=lambda kv: (kv[1]['zone'], kv[1]['top'])):
    print(f"  {info['name']:25s} zone={info['zone']:15s} pos=({info['left']:4d},{info['top']:4d})")
