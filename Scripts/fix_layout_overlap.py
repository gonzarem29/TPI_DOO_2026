import json

PATH = 'C:/Users/gonza/Documents/DOO-2026/Avances/algo corregido.mdj'

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

patrones_id = 'AAAAAG17815575555990189000000058'
diagram = find_by_id(mdj, patrones_id)
views = diagram.get('ownedViews', [])

def find_view_by_model(model_id):
    for v in views:
        if v.get('model', {}).get('$ref', '') == model_id:
            return v
    return None

def clear_edges_for(vid):
    for v in views:
        vt = v.get('_type', '')
        if vt in ('UMLAssociationView', 'UMLGeneralizationView', 'UMLInterfaceRealizationView', 'UMLDependencyView'):
            head = v.get('head', {}).get('$ref', '') if isinstance(v.get('head'), dict) else ''
            tail = v.get('tail', {}).get('$ref', '') if isinstance(v.get('tail'), dict) else ''
            if head == vid or tail == vid:
                if 'points' in v:
                    del v['points']

# Fix 1: Pedido and Producto side by side
pedido = find_view_by_model('AAAAAAGdkyHxtMxtCyg=')
producto = find_view_by_model('AAAAAAGdkykdrszxzKA=')
if pedido and producto:
    producto['left'] = 320
    producto['top'] = 280
    pedido['left'] = 560
    pedido['top'] = 280
    clear_edges_for(pedido['_id'])
    clear_edges_for(producto['_id'])

# Fix 2: FabricaDao and FabricaModelo below Dao (no vertical overlap)
dao = find_view_by_model('AAAAAG17815575555990189000000007')
fabrica_dao = find_view_by_model('AAAAAG17815575555990189000000014')
fabrica_modelo = find_view_by_model('AAAAAG17815575555990189000000016')
if dao and fabrica_dao and fabrica_modelo:
    dao_bottom = dao['top'] + dao['height']
    fabrica_dao['top'] = dao_bottom + 20  # 20px gap below Dao
    fabrica_dao['left'] = 40
    fd_bottom = fabrica_dao['top'] + fabrica_dao['height']
    fabrica_modelo['top'] = fd_bottom + 20
    fabrica_modelo['left'] = 40
    clear_edges_for(fabrica_dao['_id'])
    clear_edges_for(fabrica_modelo['_id'])

with open(PATH, 'w', encoding='utf-8') as f:
    json.dump(mdj, f, ensure_ascii=False, separators=(',', ':'))
print("Layout fixes applied and saved!")
