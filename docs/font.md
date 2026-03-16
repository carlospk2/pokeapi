# Tipografía — Press Start 2P

## Fuente elegida: Press Start 2P

**Por qué:** Es la fuente pixel más usada y reconocida para juegos retro en apps modernas.
Genuinamente renderiza en grid de píxeles, monoespaciada, y está disponible como archivo
`.ttf` que se puede empaquetar directamente en el proyecto KMP.

**Fuente:** Google Fonts — https://fonts.google.com/specimen/Press+Start+2P
**Licencia:** SIL Open Font License (libre para uso comercial)
**Archivo:** `PressStart2P-Regular.ttf` (solo un peso — correcto para pixel art)

---

## Setup en Compose Multiplatform

### 1. Ubicación del archivo

```
composeApp/src/commonMain/composeResources/font/PressStart2P-Regular.ttf
```

CMP usa `composeResources/font/` para fuentes compartidas entre plataformas.

### 2. Declaración en código

```kotlin
// composeApp/src/commonMain/.../ui/theme/Type.kt
import org.jetbrains.compose.resources.Font
import pokeapi.composeapp.generated.resources.Res
import pokeapi.composeapp.generated.resources.PressStart2P_Regular

val PressStart2P = FontFamily(
    Font(Res.font.PressStart2P_Regular, weight = FontWeight.Normal)
)
```

### 3. Escala de tamaños

La fuente pixel necesita tamaños específicos para verse correcta. Escalar en múltiplos de 8px:

```kotlin
val PokeTypography = Typography(
    // Títulos de pantalla, menú principal
    displayLarge  = TextStyle(fontFamily = PressStart2P, fontSize = 16.sp, lineHeight = 24.sp),
    // Nombres de Pokémon, secciones
    headlineMedium = TextStyle(fontFamily = PressStart2P, fontSize = 12.sp, lineHeight = 20.sp),
    // Cuerpo principal: stats, texto de batalla, descripciones
    bodyLarge     = TextStyle(fontFamily = PressStart2P, fontSize = 8.sp,  lineHeight = 16.sp),
    // Labels: PP, tipos, números pequeños
    labelSmall    = TextStyle(fontFamily = PressStart2P, fontSize = 6.sp,  lineHeight = 12.sp),
    // Botones
    labelLarge    = TextStyle(fontFamily = PressStart2P, fontSize = 8.sp,  lineHeight = 16.sp),
)
```

**Regla:** `lineHeight` siempre = `fontSize * 2` para pixel fonts.

---

## Pixel Scaling para Sprites

En todos los `Image` composables que muestren sprites de Pokémon, usar nearest-neighbor:

```kotlin
AsyncImage(
    model = ImageRequest.Builder(LocalContext.current)
        .data(spriteSource)
        .build(),
    contentDescription = null,
    filterQuality = FilterQuality.None,  // CRÍTICO: nearest-neighbor, no bilinear
    contentScale = ContentScale.Fit
)
```

`FilterQuality.None` está disponible en Compose desde 1.2 y funciona en KMP.

---

## Tamaños de sprites en pantalla

- Lista Pokédex (grid): 48x48dp
- Detalle Pokédex (grande): 128x128dp
- Batalla oponente (sprite frontal): 96x96dp
- Batalla jugador (sprite frontal): 96x96dp
- Party/equipo (miniatura): 40x40dp

Todos escalados con `FilterQuality.None`.

---

## Descarga del archivo TTF

Descargar manualmente desde Google Fonts y colocar en:
`composeApp/src/commonMain/composeResources/font/PressStart2P-Regular.ttf`

O usar el siguiente link directo (al momento de setup):
https://fonts.gstatic.com/s/pressstart2p/v15/e3t4euO8T-267oIAQAu6jDQyK3nVivM.woff2
(convertir a TTF si se descarga en woff2)

**Recomendado:** Descargar el ZIP completo de Google Fonts que incluye el TTF directamente.
