import json
import copy

PATH = 'C:/Users/gonza/Documents/DOO-2026/Avances/algo corregido.mdj'
BACKUP_PATH = 'C:/Users/gonza/Documents/DOO-2026/Avances/algo corregido BACKUP2.mdj'

with open(PATH, 'r', encoding='utf-8') as f:
    mdj = json.load(f)

def find_by_id(elem, target_id):
    if isinstance(elem, dict):
        if elem.get('_id') == target_id:
            return elem
        for key in ['ownedElements', 'ownedViews', 'subViews']:
            if key in elem:
                result = find_by_id(elem[key], target_id)
                if result:
                    return result
    elif isinstance(elem, list):
        for item in elem:
            result = find_by_id(item, target_id)
            if result:
                return result
    return None

def find_all(elem, target_type, results):
    if isinstance(elem, dict):
        if elem.get('_type') == target_type:
            results.append(elem)
        for key in ['ownedElements', 'ownedViews', 'subViews', 'children']:
            if key in elem:
                find_all(elem[key], target_type, results)
    elif isinstance(elem, list):
        for item in elem:
            find_all(item, target_type, results)

# Get all classes and interfaces by ID
all_classes = []
find_all(mdj, 'UMLClass', all_classes)
all_interfaces = []
find_all(mdj, 'UMLInterface', all_interfaces)

model_names = {}
for c in all_classes:
    model_names[c['_id']] = c.get('name', '?')
for c in all_interfaces:
    model_names[c['_id']] = c.get('name', '?')

print("=== ALL MODELS IN PROJECT ===")
for mid, name in sorted(model_names.items(), key=lambda x: x[1]):
    print(f"  {name:30s} id={mid}")

# Find Patrones de Diseno diagram
patrones_id = 'AAAAAG17815575555990189000000058'
diagram = find_by_id(mdj, patrones_id)
views = diagram.get('ownedViews', [])

# Map view to model
print(f"\n=== VIEWS IN PATRONES DE DISENO ===")
view_info = {}  # view_id -> {view, model_id, model_name, width, height}
for v in views:
    vt = v.get('_type', '')
    vid = v.get('_id', '')
    mid = v.get('model', {}).get('$ref', '') if isinstance(v.get('model'), dict) else ''
    mname = model_names.get(mid, '(unknown)')
    w = v.get('width', 150)
    h = v.get('height', 60)
    view_info[vid] = {
        'view': v,
        'model_id': mid,
        'model_name': mname,
        'width': w,
        'height': h,
        'type': vt
    }
    if vt == 'UMLClassView':
        print(f"  VIEW {vid} -> {mname:30s} pos=({v.get('left')},{v.get('top')}) size=({w}x{h}) model={mid}")
    elif 'Edge' in vt or 'Generalization' in vt or 'Association' in vt or 'Dependency' in vt or 'Realization' in vt:
        head = v.get('head', {}).get('$ref', '') if isinstance(v.get('head'), dict) else ''
        tail = v.get('tail', {}).get('$ref', '') if isinstance(v.get('tail'), dict) else ''
        hname = view_info.get(head, {}).get('model_name', '?') if head else '?'
        tname = view_info.get(tail, {}).get('model_name', '?') if tail else '?'
        print(f"  EDGE {vt:30s} {hname:20s} <- {tname}")

# ============================================================
# LAYOUT PLAN
# ============================================================
# Zone 1: DAO + Factory Method (x=40)
# Zone 2: MVC Model (x=560)
# Zone 3: State (x=920)
# Zone 4: Strategy + Observer (x=1200)
#
# Grid: col * COL_W + ORIGIN_X
#       row * ROW_H + ORIGIN_Y
# ============================================================

COL_W = 200       # horizontal spacing between columns within a zone
ZONE_GAP = 280    # extra gap between zones  
ROW_H = 100       # vertical spacing between rows
ORIGIN_X = 40
ORIGIN_Y = 40

# Layout definition: model_name -> (zone, col_in_zone, row)
# zone 0 = DAO+Factory, zone 1 = MVC, zone 2 = State, zone 3 = Strategy/Observer
# Row 0 is the top (interface/abstract), rows below are implementations

layout = {
    # Zone 0: DAO + Factory Method (x = 40 + zone*280)
    'Dao':               (0, 0, 0),   # Interface at top
    'FabricaDao':        (0, 0, 1),   # Factory depends on Dao
    'FabricaModelo':     (0, 0, 2),   # Factory depends on Modelo
    'ClienteDao':        (0, 1, 1),   # DAO implementations
    'ProductoDao':       (0, 1, 2),
    'PedidoDao':         (0, 1, 3),
    'ZonaDao':           (0, 1, 4),
    'DistribuidorDao':   (0, 1, 5),

    # Zone 1: MVC Model (x = 40 + 1*320)
    'Modelo':            (1, 0, 0),   # Abstract at top
    'Cliente':           (1, 0, 1),   # Concrete models
    'Pedido':            (1, 0, 2),
    'Producto':          (1, 0, 3),

    # Zone 2: State (x = 40 + 2*320)
    'PedidoEstado':      (2, 0, 0),   # Interface at top
    'PedidoPendiente':   (2, 0, 1),
    'PedidoConfirmado':  (2, 0, 2),
    'PedidoEnPreparacion': (2, 0, 3),
    'PedidoEnReparto':   (2, 0, 4),
    'PedidoEntregado':   (2, 0, 5),
    'PedidoCancelado':   (2, 0, 6),

    # Zone 3: Strategy + Observer (x = 40 + 3*320)
    'EstrategiaPrecio':  (3, 0, 0),   # Strategy interface
    'EstrategiaMayorista': (3, 0, 1),
    'EstrategiaMinorista': (3, 0, 2),
    'Observador':        (3, 1, 0),   # Observer interface (same zone, next col)
    'NotificacionEmail': (3, 1, 1),
    'NotificacionSMS':   (3, 1, 2),
}

# Apply positions
for vid, info in view_info.items():
    if info['type'] != 'UMLClassView':
        continue
    name = info['model_name']
    if name not in layout:
        print(f"  WARNING: {name} not in layout, skipping")
        continue
    zone, col, row = layout[name]
    x = ORIGIN_X + zone * ZONE_GAP + col * COL_W
    y = ORIGIN_Y + row * ROW_H
    v = info['view']
    old_x, old_y = v.get('left'), v.get('top')
    v['left'] = x
    v['top'] = y
    print(f"  MOVED {name:25s} ({old_x},{old_y}) -> ({x},{y})")

# Remove points on all edge views for auto-routing
# StarUML CRASHES with points: [] (empty array); removing the field avoids the crash
edge_count = 0
for vid, info in view_info.items():
    if info['type'] in ('UMLGeneralizationView', 'UMLInterfaceRealizationView',
                        'UMLAssociationView', 'UMLDependencyView'):
        v = info['view']
        if 'points' in v:
            del v['points']
        edge_count += 1

print(f"\nCleared points on {edge_count} edge views for auto-routing")

# Save
with open(PATH, 'w', encoding='utf-8') as f:
    json.dump(mdj, f, ensure_ascii=False, separators=(',', ':'))

print(f"\nSaved to {PATH}")
print("Done!")
