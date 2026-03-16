# Estrategia de Testing

## Principio

Tests enfocados en lógica de negocio pura. La prioridad es el battle engine y los cálculos —
son deterministas y críticos para la correctitud del juego.

---

## Ubicación

```
shared/src/commonTest/kotlin/com/example/pokeapi/shared/
├── features/
│   └── battle/
│       ├── StatsCalculatorTest.kt
│       ├── DamageCalculatorTest.kt
│       ├── TypeEffectivenessCalculatorTest.kt
│       ├── BattleEngineTest.kt
│       └── BattleAiTest.kt
└── features/
    └── sync/
        └── MoveFilterTest.kt
```

---

## Tests requeridos por módulo

### Módulo 2.2 — StatsCalculatorTest

```kotlin
class StatsCalculatorTest {

    // HP formula: ((2 * base * level) / 100) + level + 10
    @Test fun `HP bulbasaur level 50 is correct`() {
        // base HP = 45, level = 50
        // ((2*45*50)/100) + 50 + 10 = 45 + 50 + 10 = 105
        assertEquals(105, StatsCalculator.calcHp(base = 45, level = 50))
    }

    @Test fun `HP charizard level 100 is correct`() {
        // base HP = 78, level = 100
        // ((2*78*100)/100) + 100 + 10 = 156 + 110 = 266
        assertEquals(266, StatsCalculator.calcHp(base = 78, level = 100))
    }

    // Other stats formula: ((2 * base * level) / 100) + 5
    @Test fun `Attack pikachu level 50 is correct`() {
        // base attack = 55, level = 50
        // ((2*55*50)/100) + 5 = 55 + 5 = 60
        assertEquals(60, StatsCalculator.calcStat(base = 55, level = 50))
    }

    @Test fun `Speed at level 1`() {
        // base speed = 100, level = 1
        // ((2*100*1)/100) + 5 = 2 + 5 = 7
        assertEquals(7, StatsCalculator.calcStat(base = 100, level = 1))
    }
}
```

### Módulo 2.3 — DamageCalculatorTest

```kotlin
class DamageCalculatorTest {

    // Formula: floor((floor(2*level/5+2) * power * atk/def) / 50 + 2) * stab * typeEff * rand
    // Para tests usar random = 1.0 (máximo) o 0.85 (mínimo)

    @Test fun `Normal damage without STAB or type advantage`() {
        val dmg = DamageCalculator.calculate(
            level = 50, power = 80, attack = 100, defense = 100,
            stab = false, typeEffectiveness = 1.0f, random = 1.0f
        )
        // floor(2*50/5+2) = floor(22) = 22
        // floor(22 * 80 * 100/100) / 50 + 2 = floor(1760/50) + 2 = 35 + 2 = 37
        // * 1.0 * 1.0 * 1.0 = 37
        assertEquals(37, dmg)
    }

    @Test fun `STAB bonus is 1_5x`() {
        val noStab = DamageCalculator.calculate(
            level = 50, power = 80, attack = 100, defense = 100,
            stab = false, typeEffectiveness = 1.0f, random = 1.0f
        )
        val withStab = DamageCalculator.calculate(
            level = 50, power = 80, attack = 100, defense = 100,
            stab = true, typeEffectiveness = 1.0f, random = 1.0f
        )
        assertEquals((noStab * 1.5f).toInt(), withStab)
    }

    @Test fun `Super effective is 2x`() {
        val normal = DamageCalculator.calculate(50, 80, 100, 100, false, 1.0f, 1.0f)
        val superEffective = DamageCalculator.calculate(50, 80, 100, 100, false, 2.0f, 1.0f)
        assertEquals(normal * 2, superEffective)
    }

    @Test fun `Immune (0x) always deals 0 damage`() {
        val dmg = DamageCalculator.calculate(100, 150, 200, 50, true, 0.0f, 1.0f)
        assertEquals(0, dmg)
    }

    @Test fun `Min damage with random 0_85`() {
        val dmg = DamageCalculator.calculate(50, 80, 100, 100, false, 1.0f, 0.85f)
        assertTrue(dmg > 0)
    }
}
```

### Módulo 2.3 — TypeEffectivenessCalculatorTest

```kotlin
class TypeEffectivenessCalculatorTest {

    private val typeChart = mapOf(
        "fire" to "grass" to 2.0f,
        "water" to "fire" to 2.0f,
        "grass" to "fire" to 0.5f,
        "normal" to "ghost" to 0.0f,
    )

    @Test fun `Fire vs grass is super effective`() {
        val effectiveness = TypeEffectivenessCalculator.calculate(
            moveType = "fire",
            defenderTypes = listOf("grass"),
            chart = typeChart
        )
        assertEquals(2.0f, effectiveness)
    }

    @Test fun `Normal vs ghost is immune`() {
        val effectiveness = TypeEffectivenessCalculator.calculate(
            moveType = "normal",
            defenderTypes = listOf("ghost"),
            chart = typeChart
        )
        assertEquals(0.0f, effectiveness)
    }

    @Test fun `Dual type multiplies both`() {
        // Water vs fire-grass: 2.0 * 0.5 = 1.0
        val effectiveness = TypeEffectivenessCalculator.calculate(
            moveType = "water",
            defenderTypes = listOf("fire", "grass"),
            chart = typeChart
        )
        assertEquals(1.0f, effectiveness)
    }

    @Test fun `Missing entry defaults to 1_0`() {
        val effectiveness = TypeEffectivenessCalculator.calculate(
            moveType = "normal",
            defenderTypes = listOf("normal"),
            chart = typeChart
        )
        assertEquals(1.0f, effectiveness)
    }
}
```

### Módulo 2.3 — BattleEngineTest

```kotlin
class BattleEngineTest {

    // El engine debe aceptar un seed de random para tests deterministas
    // BattleEngine(random: (min: Double, max: Double) -> Double = { min, max -> Random.nextDouble(min, max) })

    @Test fun `A pokemon with 0 HP is fainted`()

    @Test fun `Attack kills pokemon triggers faint`()

    @Test fun `Faster pokemon attacks first`()

    @Test fun `Higher priority move goes first regardless of speed`()

    @Test fun `PP decrements after using move`()

    @Test fun `Move with 0 PP cannot be selected`()

    @Test fun `Switching pokemon consumes turn`()

    @Test fun `Battle ends when one side has no pokemon with HP`()

    @Test fun `STAB is applied when types match`()

    @Test fun `Accuracy miss skips damage`()
        // Con random simulado que siempre falla el accuracy check

    @Test fun `Turn history records each action`()
}
```

### Módulo 2.4 — BattleAiTest

```kotlin
class BattleAiTest {

    @Test fun `Easy AI never selects move with 0 PP`()

    @Test fun `Medium AI selects super effective move when available`()

    @Test fun `Hard AI selects move with highest estimated damage`()

    @Test fun `AI produces valid action in all battle states`()
}
```

### MoveFilterTest (Sync)

```kotlin
class MoveFilterTest {

    @Test fun `Status moves are filtered out`() {
        // category = "status" → excluded
    }

    @Test fun `Moves with null power are filtered out`()

    @Test fun `Moves with power > 0 are included`()
}
```

---

## Cómo correr

```bash
./gradlew :shared:testDebugUnitTest
```

---

## Convenciones

- Nombres de test en backticks con descripción en español o inglés, lo que sea más claro
- Un assert por test (o asserts relacionados al mismo comportamiento)
- Usar `assertEquals`, `assertTrue`, `assertFalse` de kotlin.test
- No mockear Room — el engine no depende de Room directamente
- El `BattleEngine` recibe una función `random` inyectable para tests deterministas
