import json, os
from collections import defaultdict

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

def get_model_name(mdj, ref):
    m = fb(mdj, ref)
    return m.get('name','?') if m else '?'

patrones = fb(mdj, 'AAAAAG17815575555990189000000058')
views = patrones.get('ownedViews', [])

# Build class view lookup with model names
class_views = {}
for v in views:
    if v.get('_type') == 'UMLClassView':
        cid = v.get('_id','')
        mid = v.get('model',{}).get('$ref','')
        name = get_model_name(mdj, mid)
        class_views[cid] = {
            'name': name, 'id': cid,
            'left': v.get('left',0), 'top': v.get('top',0),
            'width': v.get('width',100), 'height': v.get('height',80),
            'right': v.get('left',0) + v.get('width',100),
            'bottom': v.get('top',0) + v.get('height',80),
            'cx': v.get('left',0) + v.get('width',100)//2,
            'cy': v.get('top',0) + v.get('height',80)//2,
        }

# Collect edges grouped by head class
edge_types = ('UMLGeneralizationView','UMLAssociationView','UMLInterfaceRealizationView','UMLDependencyView')
head_groups = defaultdict(list)

for v in views:
    if v.get('_type') not in edge_types: continue
    hid = v.get('head',{}).get('$ref','')
    tid = v.get('tail',{}).get('$ref','')
    if hid in class_views and tid in class_views:
        head_groups[hid].append(v)

def route_edge(t_info, h_info, idx, total):
    """Compute clean edge points by edge connection type."""
    t, h = t_info, h_info
    dx = h['cx'] - t['cx']
    dy = h['cy'] - t['cy']
    same_col = abs(dx) < (t['width'] + h['width']) // 3

    # VERTICAL: tail below head, same column — subclass->superclass
    if same_col and dy < -30:
        spread = max(total - 1, 1)
        frac = (idx / spread) if spread > 0 else 0.5
        # Distribute across head's bottom edge
        hx = h['left'] + 15 + frac * (h['width'] - 30)
        sx = t['left'] + 15 + frac * (t['width'] - 30)
        return '%d:%d;%d:%d' % (int(sx), t['top'], int(hx), h['bottom'])

    # VERTICAL UPWARD: tail above head, same column
    if same_col and dy > 30:
        spread = max(total - 1, 1)
        frac = (idx / spread) if spread > 0 else 0.5
        hx = h['left'] + 15 + frac * (h['width'] - 30)
        sx = t['left'] + 15 + frac * (t['width'] - 30)
        return '%d:%d;%d:%d' % (int(sx), t['bottom'], int(hx), h['top'])

    # HORIZONTAL LEFT→RIGHT
    if dx > 100:
        spread = max(total - 1, 1)
        frac = (idx / spread) if spread > 0 else 0.5
        h_conn_y = h['top'] + 15 + frac * (h['height'] - 30)
        t_conn_y = t['top'] + 15 + frac * (t['height'] - 30)
        return '%d:%d;%d:%d' % (t['right'], int(t_conn_y), h['left'], int(h_conn_y))

    # HORIZONTAL RIGHT→LEFT
    if dx < -100:
        spread = max(total - 1, 1)
        frac = (idx / spread) if spread > 0 else 0.5
        h_conn_y = h['top'] + 15 + frac * (h['height'] - 30)
        t_conn_y = t['top'] + 15 + frac * (t['height'] - 30)
        return '%d:%d;%d:%d' % (t['left'], int(t_conn_y), h['right'], int(h_conn_y))

    # DIAGONAL: 3-point with midpoint offset
    mx = (t['cx'] + h['cx']) // 2
    my = (t['cy'] + h['cy']) // 2
    return '%d:%d;%d:%d;%d:%d' % (t['cx'], t['cy'], mx, my, h['cx'], h['cy'])

# Process edges: distribute per head group
processed = 0
for v in views:
    if v.get('_type') not in edge_types: continue
    hid = v.get('head',{}).get('$ref','')
    tid = v.get('tail',{}).get('$ref','')
    if hid not in class_views or tid not in class_views: continue

    group = head_groups[hid]
    group.sort(key=lambda e: class_views[e.get('tail',{}).get('$ref','')]['cy'])
    idx = group.index(v)
    total = len(group)

    v['points'] = route_edge(class_views[tid], class_views[hid], idx, total)
    processed += 1

with open(PATH, 'w', encoding='utf-8') as f:
    json.dump(mdj, f, ensure_ascii=False, separators=(',', ':'))

print("Routed %d edges with distributed connection points" % processed)
print("Saved (%d bytes)" % os.path.getsize(PATH))

# Show summary
print("\n=== EDGE ROUTING SUMMARY ===")
for v in views:
    if v.get('_type') not in edge_types: continue
    hid = v.get('head',{}).get('$ref','')
    tid = v.get('tail',{}).get('$ref','')
    print('%-20s -> %-20s : %s' % (
        class_views.get(tid,{}).get('name','?'),
        class_views.get(hid,{}).get('name','?'),
        v.get('points','?')))
