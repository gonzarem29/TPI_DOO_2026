import json

PATH = 'C:/Users/gonza/Documents/DOO-2026/Avances/algo corregido.mdj'

with open(PATH, 'r', encoding='utf-8') as f:
    mdj = json.load(f)

def find_by_id(elem, tid):
    if isinstance(elem, dict):
        if elem.get('_id') == tid:
            return elem
        for k in ['ownedElements', 'ownedViews', 'subViews']:
            if k in elem:
                r = find_by_id(elem[k], tid)
                if r: return r
    elif isinstance(elem, list):
        for i in elem:
            r = find_by_id(i, tid)
            if r: return r
    return None

# Print the top-level structure
print("=== TOP LEVEL ===")
for i, elem in enumerate(mdj.get('ownedElements', [])):
    t = elem.get('_type', '?')
    n = elem.get('name', '?')
    eid = elem.get('_id', '?')
    children = len(elem.get('ownedElements', []))
    has_diagrams = any(c.get('_type', '').endswith('Diagram') for c in elem.get('ownedElements', []))
    print(f"  [{i}] {t:20s} name='{n}' id={eid} children={children} has_diagrams={has_diagrams}")

# Specifically find the logical view and see its structure
for i, elem in enumerate(mdj.get('ownedElements', [])):
    if elem.get('name') == 'Logical View':
        print(f"\n=== LOGICAL VIEW STRUCTURE ===")
        for j, child in enumerate(elem.get('ownedElements', [])):
            ct = child.get('_type', '?')
            cn = child.get('name', '?')
            cid = child.get('_id', '?')
            print(f"  [{j}] {ct:25s} name='{cn}' id={cid}")
            if child.get('_type') == 'UMLClassDiagram':
                vcount = len(child.get('ownedViews', []))
                print(f"       -> {vcount} views")
        break

# Check what the Patrones de Diseno diagram parent points to
patrones = find_by_id(mdj, 'AAAAAG17815575555990189000000058')
if patrones:
    parent_ref = patrones.get('_parent', {}).get('$ref', '')
    parent = find_by_id(mdj, parent_ref)
    print(f"\n=== PATRONES DE DISENO PARENT ===")
    print(f"Parent ref: {parent_ref}")
    if parent:
        print(f"Parent type: {parent.get('_type')}")
        print(f"Parent name: {parent.get('name', '?')}")
        # Check if the diagram is listed in the parent's ownedElements
        if 'ownedElements' in parent:
            diag_ids = [c.get('_id') for c in parent['ownedElements']]
            if patrones['_id'] in diag_ids:
                print("Diagram IS listed in parent's ownedElements")
            else:
                print("WARNING: Diagram NOT listed in parent's ownedElements!")
        if 'ownedViews' in parent:
            print("Parent has ownedViews (it's a diagram container)")
    else:
        print("PARENT NOT FOUND IN DOCUMENT!")

# Same for Mermaid diagram
mermaid = find_by_id(mdj, 'AAAAAAGezTH5TbbesdE=')
if mermaid:
    parent_ref = mermaid.get('_parent', {}).get('$ref', '')
    parent = find_by_id(mdj, parent_ref)
    print(f"\n=== MERMAID DIAGRAM PARENT ===")
    print(f"Parent ref: {parent_ref}")
    if parent:
        print(f"Parent type: {parent.get('_type')}")
        print(f"Parent name: {parent.get('name', '?')}")
        if 'ownedElements' in parent:
            if mermaid['_id'] in [c.get('_id') for c in parent['ownedElements']]:
                print("Diagram IS listed in parent's ownedElements")
            else:
                print("WARNING: Diagram NOT listed in parent's ownedElements!")
    else:
        print("PARENT NOT FOUND IN DOCUMENT!")

# Check: does the pattern diagram have the correct parent structure?
# In StarUML, a UMLClassDiagram should be inside a UMLModel (e.g., "Logical View")
# AND its model references should point to UMLClass elements that are inside
# that same UMLModel's ownedElements

print(f"\n=== CHECKING: Are pattern model elements inside Logical View? ===")
lv = None
for elem in mdj.get('ownedElements', []):
    if elem.get('name') == 'Logical View':
        lv = elem
        break

if lv:
    lv_model_ids = set()
    def collect_model_ids(elem):
        if isinstance(elem, dict):
            if elem.get('_type') in ('UMLClass', 'UMLInterface', 'UMLEnumeration',
                                      'UMLAssociation', 'UMLGeneralization', 'UMLDependency'):
                lv_model_ids.add(elem.get('_id'))
            for k in ['ownedElements']:
                if k in elem:
                    collect_model_ids(elem[k])
        elif isinstance(elem, list):
            for i in elem:
                collect_model_ids(i)
    collect_model_ids(lv)
    
    # Now check each view in Patrones de Diseno
    for v in patrones.get('ownedViews', []):
        mid = v.get('model', {}).get('$ref', '') if isinstance(v.get('model'), dict) else ''
        if mid and mid not in lv_model_ids:
            model = find_by_id(mdj, mid)
            mname = model.get('name', '?') if model else 'NOT FOUND'
            print(f"  MODEL NOT IN LV: {mname} ({mid})")

print("\nDone!")
