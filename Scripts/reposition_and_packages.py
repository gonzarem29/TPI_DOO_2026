import json, os, uuid
from collections import defaultdict

PATH = 'C:/Users/gonza/Documents/DOO-2026/Avances/algo corregido.mdj'
BACKUP = 'C:/Users/gonza/Documents/DOO-2026/Avances/algo corregido BACKUP3.mdj'

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

def extract_name(v):
    for sv in v.get('subViews',[]):
        if sv.get('_type')=='UMLNameCompartmentView':
            for lv in sv.get('subViews',[]):
                if lv.get('_type')=='LabelView':
                    t=lv.get('text','')
                    if t and not t.startswith('(') and t not in ('\u00abname\u00bb','\u00abinterface\u00bb'):
                        return t
    return '?'

def gen_id():
    return 'AAAAAA' + uuid.uuid4().hex[:28]

patrones = fb(mdj, 'AAAAAG17815575555990189000000058')
logical_view = fb(mdj, 'AAAAAAFFxl9rHz9obNs=')
views = patrones.get('ownedViews', [])
logical_owned = logical_view.get('ownedElements', [])

# Read all class views
class_views = {}
for v in views:
    if v.get('_type') == 'UMLClassView':
        cid = v.get('_id','')
        class_views[cid] = {
            'name': extract_name(v),
            'width': v.get('width', 100),
            'height': v.get('height', 80),
        }

name_to_cid = {info['name']: cid for cid, info in class_views.items()}

# Define packages with class membership and layout
GAP = 30          # vertical gap between classes
COL_GAP = 40      # gap between package columns
PAD = 20          # padding inside package box
HEAD_H = 30       # header height for package name tab
LEFT_MARGIN = 30  # left margin of diagram

packages = [
    {
        'name': 'DAO+Factory',
        'classes': ['Dao', 'FabricaDao', 'FabricaModelo',
                     'ClienteDao', 'ProductoDao', 'PedidoDao',
                     'ZonaDao', 'DistribuidorDao'],
        'cols': [
            {'x': 0, 'classes': ['Dao', 'FabricaDao', 'FabricaModelo']},
            {'x': 1, 'classes': ['ClienteDao', 'ProductoDao', 'PedidoDao',
                                  'ZonaDao', 'DistribuidorDao']},
        ],
    },
    {
        'name': 'MVC',
        'classes': ['Modelo', 'Cliente', 'Producto', 'Pedido'],
        'cols': [
            {'x': 0, 'classes': ['Modelo', 'Cliente', 'Producto', 'Pedido']},
        ],
    },
    {
        'name': 'State',
        'classes': ['PedidoEstado', 'PedidoPendiente', 'PedidoConfirmado',
                     'PedidoEnPreparacion', 'PedidoEnReparto',
                     'PedidoEntregado', 'PedidoCancelado'],
        'cols': [
            {'x': 0, 'classes': ['PedidoEstado', 'PedidoPendiente', 'PedidoConfirmado',
                                  'PedidoEnPreparacion', 'PedidoEnReparto',
                                  'PedidoEntregado', 'PedidoCancelado']},
        ],
    },
    {
        'name': 'Strategy',
        'classes': ['EstrategiaPrecio', 'EstrategiaMayorista', 'EstrategiaMinorista'],
        'cols': [
            {'x': 0, 'classes': ['EstrategiaPrecio', 'EstrategiaMayorista', 'EstrategiaMinorista']},
        ],
    },
    {
        'name': 'Observer',
        'classes': ['Observador', 'NotificacionEmail', 'NotificacionSMS'],
        'cols': [
            {'x': 0, 'classes': ['Observador', 'NotificacionEmail', 'NotificacionSMS']},
        ],
    },
]

# Compute column widths and positions
cur_x = LEFT_MARGIN
for pkg in packages:
    # Determine max width per column in this package
    col_widths = []
    for col in pkg['cols']:
        max_w = 0
        for cname in col['classes']:
            if cname in name_to_cid:
                cid = name_to_cid[cname]
                w = class_views[cid]['width']
                if w > max_w: max_w = w
        col_widths.append(max_w)
    
    # Position each column
    col_starts = []
    for i, cw in enumerate(col_widths):
        col_starts.append(cur_x + (sum(col_widths[:i]) if i == 0 else sum(col_widths[:i]) + 40))
        # Actually, simpler: just place columns sequentially
    # Reset: place cols sequentially
    cx = cur_x
    col_x_starts = []
    for cw in col_widths:
        col_x_starts.append(cx)
        cx += cw + COL_GAP
    pkg['col_x'] = col_x_starts
    pkg['col_widths'] = col_widths
    pkg['start_x'] = cur_x
    
    # Calculate total width of this package
    pkg_width = max(col_widths) * len(col_widths) + COL_GAP * (len(col_widths) - 1) + 2 * PAD
    
    # Find widest column for box width
    # Actually the total width is from first col start to last col end
    if col_x_starts:
        last_col_end = col_x_starts[-1] + col_widths[-1]
        pkg['box_w'] = last_col_end - cur_x + 2 * PAD
        pkg['box_x'] = cur_x - PAD
    else:
        pkg['box_w'] = 200
        pkg['box_x'] = cur_x
    
    cur_x += pkg_width + COL_GAP

# Now position each class
new_positions = {}
for pkg in packages:
    # Build vertical layout per column
    col_current_y = []
    for i, col in enumerate(pkg['cols']):
        y = 60  # Start Y
        for cname in col['classes']:
            if cname in name_to_cid:
                cid = name_to_cid[cname]
                w = class_views[cid]['width']
                h = class_views[cid]['height']
                # Center in column
                col_w = pkg['col_widths'][i]
                x_off = (col_w - w) // 2
                x = pkg['col_x'][i] + x_off
                new_positions[cname] = (x, y)
                y += h + GAP
        col_current_y.append(y)

# Apply positions to views
for v in views:
    if v.get('_type') == 'UMLClassView':
        cid = v.get('_id', '')
        if cid in class_views:
            name = class_views[cid]['name']
            if name in new_positions:
                x, y = new_positions[name]
                v['left'] = x
                v['top'] = y
                # Also update subViews LabelView positions
                # (StarUML recalculates these, but we can leave them)

# Compute updated edge points
edge_types = ('UMLGeneralizationView','UMLAssociationView','UMLInterfaceRealizationView','UMLDependencyView')

# Build updated class info lookup
updated_info = {}
for v in views:
    if v.get('_type') == 'UMLClassView':
        cid = v.get('_id','')
        name = class_views[cid]['name']
        updated_info[cid] = {
            'name': name,
            'left': v.get('left', 0), 'top': v.get('top', 0),
            'right': v.get('left', 0) + v.get('width', 100),
            'bottom': v.get('top', 0) + v.get('height', 80),
            'width': v.get('width', 100), 'height': v.get('height', 80),
            'cx': v.get('left', 0) + v.get('width', 100)//2,
            'cy': v.get('top', 0) + v.get('height', 80)//2,
        }

head_groups = defaultdict(list)
for v in views:
    if v.get('_type') not in edge_types: continue
    hid = v.get('head',{}).get('$ref','')
    tid = v.get('tail',{}).get('$ref','')
    if hid in updated_info and tid in updated_info:
        head_groups[hid].append(v)

def route_edge(t_info, h_info, idx, total):
    t, h = t_info, h_info
    dx = h['cx'] - t['cx']
    dy = h['cy'] - t['cy']
    same_col = abs(dx) < (t['width'] + h['width']) // 2

    if same_col and dy < -30:
        spread = max(total - 1, 1)
        frac = (idx / spread) if spread > 0 else 0.5
        hx = h['left'] + 15 + frac * (h['width'] - 30)
        sx = t['left'] + 15 + frac * (t['width'] - 30)
        return '%d:%d;%d:%d' % (int(sx), t['top'], int(hx), h['bottom'])
    if dx > 100:
        spread = max(total - 1, 1)
        frac = (idx / spread) if spread > 0 else 0.5
        hy = h['top'] + 15 + frac * (h['height'] - 30)
        ty = t['top'] + 15 + frac * (t['height'] - 30)
        return '%d:%d;%d:%d' % (t['right'], int(ty), h['left'], int(hy))
    if dx < -100:
        spread = max(total - 1, 1)
        frac = (idx / spread) if spread > 0 else 0.5
        hy = h['top'] + 15 + frac * (h['height'] - 30)
        ty = t['top'] + 15 + frac * (t['height'] - 30)
        return '%d:%d;%d:%d' % (t['left'], int(ty), h['right'], int(hy))
    mx = (t['cx'] + h['cx']) // 2
    my = (t['cy'] + h['cy']) // 2
    return '%d:%d;%d:%d;%d:%d' % (t['cx'], t['cy'], mx, my, h['cx'], h['cy'])

for v in views:
    if v.get('_type') not in edge_types: continue
    hid = v.get('head',{}).get('$ref','')
    tid = v.get('tail',{}).get('$ref','')
    if hid not in updated_info or tid not in updated_info: continue
    group = head_groups[hid]
    group.sort(key=lambda e: updated_info[e.get('tail',{}).get('$ref','')]['cy'])
    idx = group.index(v)
    total = len(group)
    v['points'] = route_edge(updated_info[tid], updated_info[hid], idx, total)

# Calculate package box dimensions from new positions
print("=== Package boxes ===")
for pkg in packages:
    cids = [name_to_cid[n] for n in pkg['classes'] if n in name_to_cid]
    if not cids: continue
    min_x = min(updated_info[c]['left'] for c in cids)
    min_y = min(updated_info[c]['top'] for c in cids)
    max_x = max(updated_info[c]['right'] for c in cids)
    max_y = max(updated_info[c]['bottom'] for c in cids)
    
    bx = min_x - PAD
    by = min_y - PAD - HEAD_H
    bw = max_x - min_x + 2 * PAD
    bh = max_y - min_y + 2 * PAD + HEAD_H
    
    pkg['box'] = (bx, by, bw, bh)
    print("  %-15s box=(%d,%d,%d,%d)" % (pkg['name'], bx, by, bw, bh))

# Create UMLPackage model elements
pkg_model_ids = {}
for pkg in packages:
    pkg_id = gen_id()
    pkg_model = {
        '_type': 'UMLPackage',
        '_id': pkg_id,
        '_parent': {'$ref': logical_view.get('_id')},
        'name': pkg['name'],
    }
    logical_owned.append(pkg_model)
    pkg_model_ids[pkg['name']] = pkg_id

# Create UMLPackageView elements
for pkg in packages:
    if 'box' not in pkg: continue
    bx, by, bw, bh = pkg['box']
    
    pv_id = gen_id()
    label_id = gen_id()
    
    label_view = {
        '_type': 'LabelView',
        '_id': label_id,
        '_parent': {'$ref': pv_id},
        'font': 'Arial;13;1',
        'parentStyle': True,
        'left': bx + 5,
        'top': by + 3,
        'width': len(pkg['name']) * 8 + 16,
        'height': 15,
        'text': pkg['name'],
    }
    
    pkg_view = {
        '_type': 'UMLPackageView',
        '_id': pv_id,
        '_parent': {'$ref': patrones.get('_id')},
        'model': {'$ref': pkg_model_ids[pkg['name']]},
        'subViews': [label_view],
        'font': 'Arial;13;1',
        'parentStyle': False,
        'left': bx,
        'top': by,
        'width': bw,
        'height': bh,
        'nameLabel': {'$ref': label_id},
    }
    
    views.append(pkg_view)

with open(PATH, 'w', encoding='utf-8') as f:
    json.dump(mdj, f, ensure_ascii=False, separators=(',', ':'))

print("\nSaved (%d bytes)" % os.path.getsize(PATH))
print("Classes repositioned: %d" % len(new_positions))
print("Edges routed: %d" % sum(1 for v in views if v.get('_type') in edge_types))
print("PackageViews: %d" % sum(1 for v in views if v.get('_type') == 'UMLPackageView'))

# Show final layout
print("\n=== FINAL LAYOUT ===")
for cid, info in sorted(updated_info.items(), key=lambda kv: (kv[1]['top'], kv[1]['left'])):
    print("  %-25s pos=(%d,%d) size=%dx%d" % (
        info['name'], info['left'], info['top'],
        info['width'], info['height']))
