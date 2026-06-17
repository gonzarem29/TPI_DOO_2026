import json, os, uuid

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

def extract_class_name(v):
    """Extract class name from LabelView inside NameCompartmentView."""
    for sv in v.get('subViews', []):
        if sv.get('_type') == 'UMLNameCompartmentView':
            for lv in sv.get('subViews', []):
                if lv.get('_type') == 'LabelView':
                    t = lv.get('text', '')
                    if t and not t.startswith('(') and t != '\u00abiname\u00bb' and t != '\u00abinterface\u00bb':
                        return t
    return '?'

def gen_id():
    return 'AAAAAA' + uuid.uuid4().hex[:28]

patrones = fb(mdj, 'AAAAAG17815575555990189000000058')
logical_view = fb(mdj, 'AAAAAAFFxl9rHz9obNs=')

views = patrones.get('ownedViews', [])
logical_owned = logical_view.get('ownedElements', [])

# Build class view lookup with names from LabelViews
class_views = {}
for v in views:
    if v.get('_type') == 'UMLClassView':
        cid = v.get('_id','')
        name = extract_class_name(v)
        class_views[cid] = {
            'name': name,
            'left': v.get('left', 0),
            'top': v.get('top', 0),
            'right': v.get('left', 0) + v.get('width', 100),
            'bottom': v.get('top', 0) + v.get('height', 80),
        }

PADDING = 25
HEADER_PAD = 30

zones = [
    {
        'name': 'DAO+Factory',
        'classes': ['Dao', 'FabricaDao', 'FabricaModelo'],
    },
    {
        'name': 'MVC',
        'classes': ['Modelo', 'Cliente', 'Producto',
                     'ClienteDao', 'ProductoDao', 'PedidoDao',
                     'ZonaDao', 'DistribuidorDao'],
    },
    {
        'name': 'State',
        'classes': ['PedidoEstado', 'PedidoPendiente', 'PedidoConfirmado',
                     'Pedido', 'PedidoEnPreparacion', 'PedidoEnReparto',
                     'PedidoEntregado', 'PedidoCancelado'],
    },
    {
        'name': 'Strategy',
        'classes': ['EstrategiaPrecio', 'EstrategiaMayorista', 'EstrategiaMinorista'],
    },
    {
        'name': 'Observer',
        'classes': ['Observador', 'NotificacionEmail', 'NotificacionSMS'],
    },
]

name_to_cid = {info['name']: cid for cid, info in class_views.items()}

# Debug: print all names found
print("=== Class views found ===")
for cid, info in sorted(class_views.items(), key=lambda kv: (kv[1]['top'], kv[1]['left'])):
    print('  name=%-25s left=%-4d top=%d' % (info['name'], info['left'], info['top']))

# Compute bounding boxes
print("\n=== Zone boxes ===")
missing = []
for z in zones:
    cids = []
    for n in z['classes']:
        if n in name_to_cid:
            cids.append(name_to_cid[n])
        else:
            missing.append((z['name'], n))
    if not cids:
        print("WARNING: no classes found for zone '%s'" % z['name'])
        for n in z['classes']:
            if n in name_to_cid:
                print("  FOUND: %s" % n)
            else:
                print("  MISSING: %s (available: %s)" % (n, list(name_to_cid.keys())))
        continue
    min_l = min(class_views[c]['left'] for c in cids)
    min_t = min(class_views[c]['top'] for c in cids)
    max_r = max(class_views[c]['right'] for c in cids)
    max_b = max(class_views[c]['bottom'] for c in cids)
    
    bx = min_l - PADDING
    by = min_t - PADDING - HEADER_PAD
    bw = (max_r - min_l) + 2 * PADDING
    bh = (max_b - min_t) + 2 * PADDING + HEADER_PAD
    
    z['box'] = (bx, by, bw, bh)
    z['cids'] = cids
    print("  %-15s box=(%d,%d,%d,%d) padding=%d header=%d" % (z['name'], bx, by, bw, bh, PADDING, HEADER_PAD))

# Create UMLPackage model elements
pkg_model_ids = {}
for z in zones:
    if 'box' not in z: continue
    pkg_id = gen_id()
    pkg_model = {
        '_type': 'UMLPackage',
        '_id': pkg_id,
        '_parent': {'$ref': logical_view.get('_id')},
        'name': z['name'],
    }
    logical_owned.append(pkg_model)
    pkg_model_ids[z['name']] = pkg_id

# Create UMLPackageView elements
for z in zones:
    if 'box' not in z: continue
    bx, by, bw, bh = z['box']
    
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
        'width': len(z['name']) * 8 + 16,
        'height': 15,
        'text': z['name'],
    }
    
    pkg_view = {
        '_type': 'UMLPackageView',
        '_id': pv_id,
        '_parent': {'$ref': patrones.get('_id')},
        'model': {'$ref': pkg_model_ids[z['name']]},
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

print("\nDone. Saved (%d bytes)" % os.path.getsize(PATH))
print("\nVerification:")
count = sum(1 for v in views if v.get('_type') == 'UMLPackageView')
print("  UMLPackageView count: %d" % count)
print("  UMLPackage model count: %d" % len(pkg_model_ids))
