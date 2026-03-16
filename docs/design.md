# Dirección de Diseño - PokeAPI

## Plataforma y Modo

- **Plataforma:** Mobile (Android + iOS) con Compose Multiplatform
- **Tema:** Dark preferente, con acentos vibrantes sobre fondos oscuros (fiel a la estética de jugar GBA de noche)
- **Densidad:** Cómoda — los elementos deben ser fáciles de tocar en mobile, pero sin desperdiciar espacio. El pixel art necesita aire para respirar.

---

## Personalidad Visual

La app se siente como abrir un Game Boy Advance en 2026. No es una réplica literal del hardware — es la *memoria emocional* de jugar Pokémon Ruby/Sapphire/Emerald, filtrada a través de una app moderna. Los píxeles son intencionales, no accidentales. Los bordes son duros y definidos. Los colores son saturados pero no estridentes.

La personalidad es la de un juego que se toma en serio su propia estética pero no a sí mismo. Hay confianza en cada elemento: los menús no piden disculpas por ser pixelados, las transiciones no intentan parecer iOS nativo. Todo dice "esto es un juego retro y está orgulloso de serlo."

El balance está entre la nostalgia genuina y la usabilidad moderna. Los tap targets son generosos aunque visualmente parezcan botones de 2003. El scroll es fluido aunque la tipografía sea bitmap. La información se presenta con claridad moderna dentro de un envoltorio retro.

---

## Dirección de Color

- **Tono general:** Oscuro y saturado. Fondos profundos que hacen que los sprites y los colores de tipo brillen como en una pantalla retroiluminada.
- **Color primario:** Un azul profundo que evoque las ventanas de diálogo de Pokémon Gen III — no eléctrico, sino el azul denso de las cajas de texto de Ruby/Sapphire. Confiable, familiar, inmediatamente reconocible para quien jugó esa generación.
- **Acentos:** Los colores de tipo Pokémon son los verdaderos acentos de la app. Fuego es naranja cálido, Agua es azul claro, Planta es verde vivo, Eléctrico es amarillo puro. Estos colores aparecen en badges de tipo, fondos de movimientos, y barras de efectividad. Son funcionales primero, decorativos segundo.
- **Estados:**
  - Éxito se siente como "¡Es super efectivo!" — un destello de impacto positivo, breve y claro
  - Error se siente como un movimiento que falló — no catastrófico, pero inequívoco
  - Warning es el amarillo de una barra de HP a media vida — atención sin alarma
  - Info es neutro y contenido, como el texto de la Pokédex describiendo un hábitat
- **Fondos:** Oscuros con textura sutil. No negro puro — más bien el gris azulado oscuro de una pantalla GBA en penumbra. Las secciones se diferencian por variaciones sutiles de tono, no por colores completamente distintos.
- **Barra de HP:** Gradiente funcional clásico — verde cuando está sana, amarillo en el medio, rojo cuando es crítico. Este gradiente es sagrado, no se cambia.

---

## Dirección Tipográfica

- **Personalidad:** Pixel bitmap. La tipografía principal debe sentirse como si saliera de una consola portátil — monoespaciada o casi-monoespaciada, con cada carácter ocupando un espacio definido en una grid. No es una fuente moderna estilizada como pixel, es una fuente que genuinamente se renderiza en grid de píxeles.
- **Jerarquía:**
  - Títulos de sección (menú principal, nombre de pantalla): tamaño grande, mismo estilo pixel pero más prominente, posiblemente con sombra de 1px para profundidad
  - Cuerpo (descripciones, stats, texto de batalla): tamaño estándar legible, el workhorse de la app
  - Caption (PP restante, números pequeños, metadata): tamaño reducido pero todavía legible en pixel grid
  - Nombres de Pokémon y movimientos: siempre en mayúsculas como en los juegos originales
- **Peso visual:** Medio. La fuente pixel tiene un peso inherente — no es ligera ni pesada, es *definida*. Cada píxel está o no está. La jerarquía se logra con tamaño, no con bold/light.

---

## Dirección de Forma y Espacio

- **Esquinas:** Rectas o mínimamente redondeadas (1-2px). Los elementos retro tienen bordes duros. Las cajas de diálogo, los paneles de stats y los contenedores usan bordes definidos, no soft rounded. Si algo tiene borde redondeado, es porque imita un elemento específico del GBA (como las burbujas de HP).
- **Bordes:** Visibles y con personalidad. El estilo GBA usa bordes dobles o con highlight/shadow para simular profundidad (borde claro arriba-izquierda, borde oscuro abajo-derecha). Este efecto de "ventana 3D falsa" es un sello de la era.
- **Sombras:** No hay drop shadows modernos. La profundidad se logra con bordes highlight/shadow estilo ventana de 16-bit, o con variaciones de color de fondo. Un panel "elevado" tiene borde superior claro y borde inferior oscuro.
- **Espaciado:** Basado en grid. Todo se alinea a una grid de 8px (o el equivalente en la densidad de la pantalla). El espaciado entre elementos es consistente y predecible — como tiles en un mapa de juego.
- **Densidad de información:** Balanceada hacia densa en pantallas de datos (Pokédex, stats, selección de movimientos) y espaciosa en pantallas de acción (batalla, menú principal). La batalla necesita aire para que el jugador se enfoque; la Pokédex puede ser información-densa porque es consulta.

---

## Dirección de Iconografía

- **Estilo:** Pixel art de 16x16 o 32x32 según contexto. Los íconos son sprites, no vectores. Cada ícono se siente como un item o símbolo dentro del juego mismo.
- **Peso:** Medio — deben ser legibles a tamaño pequeño pero con personalidad suficiente para ser reconocibles.
- **Personalidad:** Funcionales con encanto. Los íconos de tipo Pokémon son el set principal (fuego, agua, planta, etc.) y deben ser inmediatamente reconocibles. Los íconos de navegación (back, menú, buscar) son simples y claros pero pixelados. No son los íconos genéricos de Material Design — tienen el carácter del juego.
- **Badges de tipo:** Cada tipo tiene su color y un ícono o símbolo representativo. Se usan extensamente: en la Pokédex, en selección de movimientos, en el HUD de batalla, en los filtros.

---

## Dirección de Interacción

- **Velocidad:** Deliberada pero no lenta. Las acciones tienen un beat de respuesta — como cuando seleccionas un movimiento en el GBA y hay un instante antes de que se ejecute. No es lag, es ritmo. Las transiciones de menú son rápidas (100-200ms), las animaciones de batalla tienen su tempo propio.
- **Feedback:**
  - Selección de menú: efecto de cursor que se mueve entre opciones (highlight visible, posiblemente con sonido corto)
  - Tap en botón: flash breve o inversión de colores del borde (el efecto de "botón presionado" de 16-bit)
  - Selección de movimiento en batalla: el movimiento se resalta, hay un beat, y luego se ejecuta
- **Transiciones:**
  - Entre pantallas principales: fade a negro corto o slide horizontal (como cambiar de menú en el PokéNav)
  - Entrada a batalla: transición especial — efecto de "encuentro" con flash o espiral (signature de Pokémon)
  - Dentro de batalla: las transiciones son del texto narrativo, no de la UI. La caja de texto guía el ritmo.
- **Microinteracciones:**
  - La barra de HP baja gradualmente, no salta
  - El texto aparece letra por letra (typewriter) con velocidad ajustable o skippeable con tap
  - Los sprites parpadean al recibir daño
  - El cursor del menú tiene un bounce sutil o animación de idle
  - Los números de daño o efectividad aparecen con un pop breve

---

## Pantallas Clave — Dirección Específica

### Pantalla de Sync Inicial
Debe sentirse como la intro de un juego GBA — no como un loading screen de app moderna. El progreso puede visualizarse como una Pokéball llenándose, una barra de HP que sube, o Pokémon caminando de izquierda a derecha conforme avanza la descarga. El texto de estado usa el estilo typewriter. Si hay error, el mensaje se presenta como un "movimiento que falló", no como un alert del sistema.

### Menú Principal
Inspiración directa: el menú de Pokémon Emerald. Opciones listadas verticalmente con un cursor que se mueve entre ellas. Fondo con patrón sutil o gradiente oscuro. Cada opción puede tener un ícono pixel art a la izquierda. La selección actual se resalta claramente. No es un grid de cards moderno — es una lista con cursor.

### Pokédex
La consulta más densa en información. Grid de sprites pequeños para la lista (3-4 columnas), con número y nombre debajo. Al seleccionar, transición al detalle que muestra sprite grande, barras de stats horizontales coloreadas (la visualización clásica de base stats), badges de tipo, y lista scrolleable de movimientos. Los filtros de tipo se presentan como badges seleccionables con los colores de tipo.

### Armado de Equipo
Los 6 slots se muestran como una party screen — vertical, con sprite, nombre, nivel y barra de HP de cada Pokémon. Los slots vacíos se ven como espacios punteados esperando ser llenados. La selección de movimientos muestra los 4 slots de movimiento con tipo, poder y PP, y el pool de movimientos disponibles como lista scrolleable.

### Pantalla de Batalla
La más importante. Layout clásico GBA:
- Mitad superior: campo de batalla con fondo pixelado, sprite del oponente arriba-derecha, sprite del jugador abajo-izquierda
- Panel del oponente: nombre, nivel, barra de HP (sin número)
- Panel del jugador: nombre, nivel, HP numérico, barra de HP
- Mitad inferior: caja de diálogo/acción con fondo azul oscuro y bordes estilo ventana GBA
- Estado default: texto narrativo con opciones "Luchar" / "Pokémon"
- Modo Luchar: grid 2x2 de movimientos, cada uno con nombre, tipo (badge de color) y PP
- Modo Pokémon: lista de party con HP y estado

### Selección de Entrenador
Lista de entrenadores organizados por tier/dificultad. Cada entrenador muestra sprite, nombre, indicador de dificultad (estrellas o badges) y récord del jugador contra él. Los tiers se separan visualmente como "rutas" o "salas" de una liga.

### Pantalla de Récords
Dashboard retro. Stats principales prominentes (W/L, racha) presentados como la pantalla de Trainer Card de los juegos. Historial como lista con íconos de victoria (verde) y derrota (rojo). Stats por entrenador accesibles como sub-pantalla.

---

## Referencias Visuales

- **Pokémon Emerald (GBA)** — por el layout de batalla, el estilo de menús, la paleta de colores de las cajas de diálogo, y el ritmo general de la interfaz
- **Pokémon FireRed/LeafGreen (GBA)** — por la Pokédex UI y la presentación de stats
- **Pokémon Stadium (N64)** — por la pantalla de selección de equipo y el concepto de batallas aisladas contra CPU
- **Undertale** — por cómo usa pixel art en interfaces modernas sin sentirse anticuado, y por el sistema de texto narrativo en batalla
- **Shovel Knight** — por la demostración de que el pixel art moderno puede ser más legible y expresivo que el original, sin perder autenticidad

---

## Lo que NO es

- NO es una app moderna con "toque retro" — el pixel art no es un skin sobre Material Design, es el lenguaje visual completo
- NO es una réplica exacta del GBA — no emulamos las limitaciones técnicas (resolución 240x160, paleta de 32K colores). Tomamos la estética y la adaptamos a pantallas modernas de alta resolución
- NO es minimalista ni flat — tiene profundidad visual a través de bordes, highlight/shadow y color. Los elementos tienen presencia
- NO usa gradientes suaves, blur, glassmorphism, ni ningún efecto visual post-2010
- NO usa la tipografía del sistema (San Francisco, Roboto) — todo texto debe ser pixel font
- Evitar cards con rounded corners grandes, FABs, bottom sheets, o cualquier patrón que grite "app de 2024"
- Evitar animaciones con curvas ease-in-out suaves — las transiciones son más abruptas y definidas, como frames de animación de sprites

---

## Notas Adicionales

- **Sprites:** PokéAPI ofrece sprites en diferentes estilos. Para máxima coherencia con la estética GBA, priorizar los sprites de generación III (Ruby/Sapphire). Si no están disponibles para todos los 386, usar los sprites default y asegurar que el renderizado en la app los muestra con nearest-neighbor scaling (sin antialiasing) para mantener los píxeles definidos.
- **Scaling de pixel art:** Crítico. Todo asset pixel art debe escalarse con nearest-neighbor interpolation, nunca con bilinear/bicubic. Un sprite borroso rompe toda la estética. En Compose, esto significa configurar el FilterQuality en los Image composables.
- **Accesibilidad:** Aunque la estética es retro, los tap targets deben cumplir el mínimo de 48dp. La barra de HP usa color + posición (no solo color) para comunicar estado. El texto narrativo debe ser legible — si la pixel font compromete legibilidad en tamaños pequeños, evaluar una fuente secundaria más clara para textos largos como descripciones de Pokédex.
- **Orientación:** Portrait fijo. La batalla y los menús se diseñan para vertical, consistente con el uso mobile.
- **Sonido (futuro):** La dirección visual está lista para complementarse con audio 8-bit/chiptune en versiones futuras. Los beats de interacción (selección, daño, victoria) tienen timing diseñado para acompañar sfx.
