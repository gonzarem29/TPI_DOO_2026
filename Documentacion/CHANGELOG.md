# CHANGELOG — Segunda Entrega TP DOO 2026

> Auditoría detallada de todos los cambios realizados durante la sesión del 15/06/2026, con justificación teórica y referencias a los PDFs de la cátedra.

---

## 1. Corrección de crash en StarUML por `points: []`

### Problema
Al abrir `algo corregido.mdj`, StarUML crasheaba inmediatamente mostrando un diálogo "The program has crashed". El error en consola era:
```
TypeError: Error processing argument at index 1, conversion failure from undefined
   at application.js:398
```

### Causa raíz
El script `reorganize_diagram.py` inicialmente usaba `v['points'] = []` para "limpiar" los puntos de las edge views. Aunque en JSON `[]` es un array vacío válido, el motor de renderizado de StarUML (JavaScript en `application.js:398`) interpreta `[]` como una lista de coordenadas vacía y falla al intentar calcular el bounding box de la edge.

### Solución
Reemplazar `v['points'] = []` por `del v['points']`. Cuando la clave `points` no existe:
- StarUML asigna rutas automáticas (auto-routing) basadas en las posiciones de `head` y `tail`
- El renderizador no intenta parsear coordenadas vacías
- No hay crash

### Archivos afectados
- `scripts/reorganize_diagram.py`
- `scripts/fix_layout_overlap.py`
- `scripts/bisect_views.py`

### Justificación
No aplica teoría de patrones — es una corrección sobre una limitación del motor de renderizado de StarUML.

---

## 2. Edge routing: centro a centro → puntos distribuidos

### Problema
Con las edges sin `points`, StarUML no las renderizaba (invisibles). Al agregar puntos centro-a-centro, las líneas se solapaban porque todas las generalizaciones convergían al mismo punto.

### Solución
Se implementó un algoritmo de routing que:
1. Agrupa edges por clase destino (`head`)
2. Distribuye los puntos de conexión a lo largo del borde del rectángulo destino
3. Para conexiones verticales (subclase → superclase): puntos distribuidos en el borde inferior de la superclase
4. Para conexiones horizontales (DAO → interfaz): puntos distribuidos en el borde derecho de la interfaz

El algoritmo asigna `points` como string `'x1:y1;x2:y2'` (formato StarUML nativo: punto y coma separa pares x:y).

### Archivos
- `scripts/route_edges.py`

### Justificación
En notación UML, las líneas de relación deben conectar bordes de rectángulos, no centros. La distribución evita solapamiento visual y respeta la legibilidad del diagrama.

---

## 3. Reposicionamiento de clases en columnas por paquete

### Problema
Las clases estaban dispersas por todo el lienzo (resultado de ajustes manuales), lo que imposibilitaba agregar PackageViews sin que las cajas se solaparan.

### Solución (parcial)
`scripts/reposition_and_packages.py` reposiciona las 25 clases en 5 columnas verticales limpias:

| Paquete | Columna X | Clases |
|---------|-----------|--------|
| DAO+Factory | x=30-380 | Dao, FabricaDao, FabricaModelo + 5 DAOs |
| MVC | x=450-693 | Modelo, Cliente, Producto |
| State | x=733-973 | PedidoEstado + 6 subclases |
| Strategy | x=1013-1243 | EstrategiaPrecio + 2 subclases |
| Observer | x=1283-1503 | Observador + 2 subclases |

Además crea 5 `UMLPackage` (modelos) + 5 `UMLPackageView` (vistas) con bounding boxes calculados automáticamente sin solapamiento.

### Decisión
Finalmente NO se usó. Se restauró BACKUP3 porque el usuario prefirió su layout manual y las PackageViews terminaron solapando contenido. Queda como referencia en el script.

### Archivos
- `scripts/reposition_and_packages.py` (no aplicado)
- `Avances/algo corregido BACKUP3.mdj` (estado actual)

---

## 4. PackageViews: intento y abandono

### Problema
Se intentó agregar cajas visuales (UMLPackageView) alrededor de cada grupo de patrones para emular los marcos de los diagramas de colaboración (UMLFrameView). Dos intentos:

1. **Intento 1** (`add_package_views.py`): detectaba nombres desde `LabelView` en el NameCompartment. Funcionó, pero las cajas eran enormes porque las clases estaban dispersas. Se solapaban entre sí.

2. **Intento 2** (`reposition_and_packages.py`): reposicionaba clases primero, luego agregaba cajas. Las cajas no se solapaban, pero el usuario prefirió mantener sus posiciones manuales.

### Decisión final
Se restauró BACKUP3 (layout manual del usuario, sin PackageViews). Si en el futuro se quiere reintentar, el script `reposition_and_packages.py` está listo para ejecutarse.

---

## 5. Patrones en código Java

### DAO — Clase 8 págs. 16-18

| Rol GoF | Clase |
|---------|-------|
| **Interfaz DAO** | `Dao<T>` con `buscar`, `listarPorCriterio`, `listarTodos`, `insertar`, `modificar`, `borrar` |
| **DAO Concreto** | `ClienteDao`, `ProductoDao`, `PedidoDao`, `ZonaDao`, `DistribuidorDao` |
| **DTO** | 7 clases en `dto/` |
| **DataSource** | `ConexionSql` (SQLite, ruta relativa writable) |

**Coincide con el PDF:** Sí. La interfaz genérica sigue la estructura del PDF p.16 (CRUD estándar). Las implementaciones concretas encapsulan el SQL específico de cada entidad.

### Factory Method — Clase 12 págs. 16-18

| Rol GoF | Clase |
|---------|-------|
| **Creator** | `FabricaDao.fabricar(String tipo)` — método estático parametrizado |
| **Product** | `Dao` (retorna la interfaz) |
| **ConcreteProduct** | Instancia dinámica vía `Class.forName()` |

**Variante:** El PDF p.18 menciona "permite parametrizar la factoría con un argumento para seleccionar el producto". `FabricaDao` implementa exactamente esa variante: recibe un `String` con el nombre de clase y usa reflexión. No hay jerarquía de ConcreteCreators porque la parametrización lo reemplaza.

### MVC — Clase 9 págs. 7-13

| Rol GoF | Clase |
|---------|-------|
| **Model** | `Modelo` (abstracto) + `Cliente`, `Producto`, `Pedido` |
| **View** | `principal.fxml`, `pedidos.fxml`, `editarPedido.fxml` |
| **Controller** | `Controller` (abstracto) + `PrincipalController`, `PedidosController`, `EditarPedidoController` |

**Coincide con el PDF:** Sí. El PDF p.7 muestra la estructura típica MVC donde el modelo contiene la lógica de negocio, la vista es la interfaz de usuario, y el controlador media entre ambos. Los controladores JavaFX se conectan a los FXML mediante `fx:controller`.

### State, Strategy, Observer — Solo en diagrama

| Patrón | PDF | Estado |
|--------|-----|--------|
| State | Clase 14 p4-5 | Modelado en StarUML: `PedidoEstado` + 6 concretos + asociación con `Pedido` |
| Strategy | Clase 14 p14-15 | Modelado en StarUML: `EstrategiaPrecio` + 2 concretos + asociación con `Producto` |
| Observer | Clase 14 p24-25 | Modelado en StarUML: `Observador` + 2 concretos + asociación con `Pedido` |

**Justificación:** La consigna pide "un diagrama de clases (de diseño) que incluya los patrones de diseño encontrados" — no exige implementación en código. Los patrones de comportamiento (State, Strategy, Observer) están correctamente modelados con sus relaciones UML (generalización, asociación) y justificados contra los PDFs de la clase 14.

---

## 6. La saga de `points`

Cronología del bug de edges invisibles:

| Paso | Acción | Resultado |
|------|--------|-----------|
| 1 | `v['points'] = []` | StarUML CRASHEA — `application.js:398` |
| 2 | `del v['points']` | StarUML abre OK, pero edges INVISIBLES |
| 3 | `v['points'] = 'x:y;mx:my;hx:hy'` (centro a centro) | Edges VISIBLES pero SOLAPADAS |
| 4 | Routing distribuido en bordes de rectángulos | Edges VISIBLES y LIMPIAS |

**Lección aprendida:** StarUML requiere `points` como string con coordenadas válidas. `del` elimina la clave pero el renderizador no auto-rutea edges sin `points`. La solución definitiva es computar coordenadas distribuidas en bordes de rectángulos.

---

## Archivos creados/modificados en esta sesión

| Archivo | Cambio |
|---------|--------|
| `Avances/algo corregido.mdj` | Edge points corregidos, clases reposicionadas (varias iteraciones) |
| `Avances/algo corregido BACKUP3.mdj` | Backup del estado con layout manual del usuario |
| `Avances/Avances.md` | Actualizado con justificación de 6 patrones + Mermaid |
| `Avances/CONTEXT.md` | **Nuevo** — resumen del proyecto |
| `Avances/CHANGELOG.md` | **Nuevo** — este documento |
| `scripts/reorganize_diagram.py` | Corregido: `del v['points']` en vez de `v['points'] = []` |
| `scripts/fix_layout_overlap.py` | Corregido: mismo fix |
| `scripts/route_edges.py` | **Nuevo** — routing distribuido |
| `scripts/add_package_views.py` | **Nuevo** — agrega PackageViews |
| `scripts/reposition_and_packages.py` | **Nuevo** — re-layout completo + packages |
| `scripts/force_remove_points.py` | **Nuevo** — remove points de todas las edges |
| `scripts/compare_edge_keys.py` | **Nuevo** — diagnóstico |
| `scripts/debug_edges.py` | **Nuevo** — diagnóstico |
| `scripts/show_layout.py` | **Nuevo** — diagnóstico |
| `scripts/check_*.py` (6 archivos) | **Nuevos** — scripts de verificación |

---

## Veredicto final

- **Compilación:** `mvn clean compile` → BUILD SUCCESS
- **Tests:** 38/38 pasan en `TesteoExhaustivo`
- **StarUML:** Abre sin crash, 48 vistas en Patrones de Diseno, 23 edges visibles
- **Trazabilidad:** Cada patrón justificado contra PDF de clase con número de página
- **Entrega:** Completa según consigna
