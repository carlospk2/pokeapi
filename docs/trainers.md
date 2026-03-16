# Entrenadores CPU — Definición de Contenido

## Estructura de datos

Los entrenadores se cargan desde un archivo Kotlin hardcodeado en:
`shared/.../shared/features/trainers/data/TrainerSeedData.kt`

Los moves se especifican por **nombre** (lowercase, igual que en PokeAPI).
Al iniciar una batalla, se resuelven contra la tabla `moves` de Room.
Si un move no existe en DB (fue filtrado por power=0), se omite silenciosamente.

---

## Tier 1 — Novato (AI: easy, level 20-30, 3 Pokémon)

### Entrenador 1: Youngster Joey
```
Pokemon 1: Rattata (id=19), level 25
  moves: [tackle, quick-attack, hyper-fang, bite]
Pokemon 2: Ekans (id=23), level 22
  moves: [poison-sting, bite, wrap, acid]  (wrap=35, acid=51)
Pokemon 3: Spearow (id=21), level 24
  moves: [peck, growl, fury-attack, aerial-ace]
```

### Entrenador 2: Lass Lily
```
Pokemon 1: Jigglypuff (id=39), level 20
  moves: [pound, body-slam, double-slap, headbutt]
Pokemon 2: Clefairy (id=35), level 22
  moves: [pound, doubleslap, headbutt, body-slam]
Pokemon 3: Meowth (id=52), level 25
  moves: [scratch, bite, pay-day, headbutt]
```

### Entrenador 3: Bug Catcher Billy
```
Pokemon 1: Caterpie (id=10), level 18
  moves: [tackle, string-shot, bug-bite]  (bug-bite=450 — puede no existir, usar tackle x2)
  moves: [tackle, bug-bite, electroweb, string-shot]  → simplificar a [tackle, tackle, tackle, tackle]
  Mejor: Beedrill (id=15) level 20, moves: [twineedle, poison-sting, fury-attack, aerial-ace]
Pokemon 1: Beedrill (id=15), level 20
  moves: [twineedle, poison-sting, fury-attack, aerial-ace]
  Note: twineedle id=41, poison-sting id=40, fury-attack id=31
Pokemon 2: Butterfree (id=12), level 22
  moves: [confusion, gust, tackle, aerial-ace]
Pokemon 3: Parasect (id=47), level 25
  moves: [scratch, fury-cutter, slash, spore] → spore es status, reemplazar
  moves: [scratch, fury-cutter, slash, bug-bite]
```

### Entrenador 4: Hiker Rocky
```
Pokemon 1: Geodude (id=74), level 28
  moves: [tackle, rock-throw, magnitude, rock-blast]
  Note: rock-throw id=88, magnitude id=222, rock-blast id=350
  Safer: [tackle, rock-throw, rollout, rock-blast]
Pokemon 2: Onix (id=95), level 28
  moves: [tackle, rock-throw, bind, slam]
  Note: bind id=20, slam id=21
Pokemon 3: Machop (id=66), level 30
  moves: [low-kick, karate-chop, seismic-toss, rock-throw]
  Note: low-kick id=67, karate-chop id=2, seismic-toss id=69
```

---

## Tier 2 — Intermedio (AI: medium, level 40-50, 4-5 Pokémon)

### Entrenador 5: Ace Trainer Blaze (Fire specialist)
```
Pokemon 1: Arcanine (id=59), level 45
  moves: [flamethrower, bite, extremespeed, iron-tail]
  Note: flamethrower=53, extremespeed=245, iron-tail=231
Pokemon 2: Rapidash (id=78), level 44
  moves: [flamethrower, fire-spin, stomp, take-down]
  Note: fire-spin=83, stomp=23, take-down=36
Pokemon 3: Magmar (id=126), level 45
  moves: [flamethrower, fire-punch, confuse-ray, smog] → confuse-ray status, smog status
  moves: [flamethrower, fire-punch, thunder-punch, body-slam]
  Note: fire-punch=7, thunder-punch=9
Pokemon 4: Charizard (id=6), level 48
  moves: [flamethrower, fire-blast, aerial-ace, slash]
  Note: fire-blast=126, aerial-ace=332, slash=163
```

### Entrenador 6: Swimmer Marina (Water specialist)
```
Pokemon 1: Vaporeon (id=134), level 43
  moves: [surf, water-gun, bite, quick-attack]
  Note: surf=57, water-gun=55
Pokemon 2: Seadra (id=117), level 44
  moves: [surf, bubble-beam, twister, agility] → agility=97 status-like
  moves: [surf, bubble-beam, twister, water-gun]
  Note: bubble-beam=61, twister=239
Pokemon 3: Cloyster (id=91), level 45
  moves: [surf, ice-beam, icicle-spear, spike-cannon]
  Note: ice-beam=58, icicle-spear=333, spike-cannon=131
Pokemon 4: Gyarados (id=130), level 48
  moves: [surf, hyper-beam, bite, dragon-rage]
  Note: hyper-beam=63, dragon-rage=82
```

### Entrenador 7: Psychic Sabrina (Psychic specialist)
```
Pokemon 1: Kadabra (id=64), level 43
  moves: [psychic, confusion, recover, psybeam] → recover=105 no power
  moves: [psychic, psybeam, headbutt, confusion]
  Note: psychic=94, psybeam=60, confusion=93
Pokemon 2: Mr. Mime (id=122), level 44
  moves: [psychic, psybeam, barrier, body-slam] → barrier no power
  moves: [psychic, psybeam, body-slam, headbutt]
Pokemon 3: Jynx (id=124), level 45
  moves: [psychic, ice-beam, blizzard, pound]
  Note: blizzard=59
Pokemon 4: Alakazam (id=65), level 48
  moves: [psychic, psybeam, recover, kinesis] → use available
  moves: [psychic, psybeam, headbutt, body-slam]
```

### Entrenador 8: Black Belt Koga (Fighting specialist)
```
Pokemon 1: Machoke (id=67), level 42
  moves: [karate-chop, low-kick, cross-chop, bulk-up] → bulk-up no power
  moves: [karate-chop, low-kick, cross-chop, rock-throw]
  Note: cross-chop=238
Pokemon 2: Primeape (id=57), level 44
  moves: [karate-chop, low-kick, cross-chop, thrash]
  Note: thrash=37
Pokemon 3: Hitmonlee (id=106), level 46
  moves: [high-jump-kick, mega-kick, jump-kick, brick-break]
  Note: high-jump-kick=136, mega-kick=25, jump-kick=26, brick-break=280
Pokemon 4: Hitmonchan (id=107), level 46
  moves: [ice-punch, fire-punch, thunder-punch, brick-break]
  Note: ice-punch=8
Pokemon 5: Machamp (id=68), level 48
  moves: [cross-chop, karate-chop, earthquake, rock-slide]
  Note: earthquake=89, rock-slide=157
```

### Entrenador 9: Ace Trainer Elena (Electric specialist)
```
Pokemon 1: Jolteon (id=135), level 44
  moves: [thunderbolt, quick-attack, pin-missile, bite]
  Note: thunderbolt=85, pin-missile=42
Pokemon 2: Electrode (id=101), level 44
  moves: [thunderbolt, spark, rollout, swift]
  Note: spark=209, rollout=205, swift=129
Pokemon 3: Magneton (id=82), level 45
  moves: [thunderbolt, swift, thunder, sonicboom] → sonicboom=49
  moves: [thunderbolt, swift, thunder, headbutt]
  Note: thunder=87
Pokemon 4: Raichu (id=26), level 47
  moves: [thunderbolt, thunder, quick-attack, iron-tail]
```

---

## Tier 3 — Avanzado (AI: medium, level 60-70, 6 Pokémon)

### Entrenador 10: Dragon Tamer Lance
```
Pokemon 1: Dragonair (id=148), level 62
  moves: [dragon-rage, slam, body-slam, thunder-wave] → thunder-wave status
  moves: [dragon-rage, slam, body-slam, ice-beam]
Pokemon 2: Dragonair (id=148), level 63
  moves: [dragon-rage, slam, body-slam, fire-blast]
Pokemon 3: Aerodactyl (id=142), level 64
  moves: [bite, aerial-ace, rock-slide, hyper-beam]
Pokemon 4: Gyarados (id=130), level 65
  moves: [surf, hyper-beam, bite, dragon-rage]
Pokemon 5: Dragonair (id=148), level 65
  moves: [dragon-rage, slam, body-slam, thunder]
Pokemon 6: Dragonite (id=149), level 68
  moves: [hyper-beam, fire-blast, thunder, blizzard]
```

### Entrenador 11: Ice Queen Lorelei
```
Pokemon 1: Dewgong (id=87), level 62
  moves: [surf, ice-beam, aurora-beam, headbutt]
  Note: aurora-beam=62
Pokemon 2: Cloyster (id=91), level 63
  moves: [surf, ice-beam, icicle-spear, spike-cannon]
Pokemon 3: Slowbro (id=80), level 63
  moves: [surf, psychic, ice-beam, headbutt]
Pokemon 4: Jynx (id=124), level 64
  moves: [psychic, ice-beam, blizzard, pound]
Pokemon 5: Lapras (id=131), level 65
  moves: [surf, ice-beam, body-slam, blizzard]
Pokemon 6: Cloyster (id=91), level 66
  moves: [surf, ice-beam, blizzard, spike-cannon]
```

### Entrenador 12: Poison Master Koga
```
Pokemon 1: Muk (id=89), level 62
  moves: [sludge-bomb, body-slam, minimize, acid] → minimize=107 no power
  moves: [sludge-bomb, body-slam, headbutt, acid]
  Note: sludge-bomb=188, acid=51
Pokemon 2: Weezing (id=110), level 63
  moves: [sludge-bomb, body-slam, tackle, smokescreen] → smokescreen no power
  moves: [sludge-bomb, body-slam, tackle, headbutt]
Pokemon 3: Venomoth (id=49), level 63
  moves: [psychic, sludge-bomb, aerial-ace, bug-buzz] → bug-buzz=405 Gen4
  moves: [psychic, sludge-bomb, aerial-ace, gust]
Pokemon 4: Gengar (id=94), level 65
  moves: [shadow-ball, sludge-bomb, psychic, night-shade]
  Note: shadow-ball=247, night-shade=101
Pokemon 5: Arbok (id=24), level 64
  moves: [poison-sting, bite, wrap, sludge-bomb]
Pokemon 6: Gengar (id=94), level 67
  moves: [shadow-ball, sludge-bomb, psychic, hyper-beam]
```

### Entrenador 13: Rock Master Brock (Avanzado)
```
Pokemon 1: Graveler (id=75), level 62
  moves: [rock-slide, earthquake, magnitude, self-destruct]
  Note: self-destruct=120
Pokemon 2: Rhydon (id=112), level 64
  moves: [earthquake, rock-slide, horn-attack, hyper-beam]
  Note: horn-attack=30
Pokemon 3: Omastar (id=139), level 63
  moves: [surf, rock-slide, spike-cannon, bite]
Pokemon 4: Golem (id=76), level 65
  moves: [earthquake, rock-slide, body-slam, hyper-beam]
Pokemon 5: Onix (id=95), level 63
  moves: [rock-slide, earthquake, slam, bind]
Pokemon 6: Aerodactyl (id=142), level 66
  moves: [rock-slide, aerial-ace, bite, hyper-beam]
```

### Entrenador 14: Ace Bruno (Balanced)
```
Pokemon 1: Scyther (id=123), level 62
  moves: [slash, aerial-ace, fury-cutter, wing-attack]
  Note: slash=163, fury-cutter=210, wing-attack=17
Pokemon 2: Pinsir (id=127), level 63
  moves: [slash, vice-grip, seismic-toss, brick-break]
  Note: vice-grip=11
Pokemon 3: Alakazam (id=65), level 64
  moves: [psychic, psybeam, headbutt, shadow-ball]
Pokemon 4: Machamp (id=68), level 65
  moves: [cross-chop, earthquake, rock-slide, hyper-beam]
Pokemon 5: Gengar (id=94), level 65
  moves: [shadow-ball, sludge-bomb, psychic, night-shade]
Pokemon 6: Exeggutor (id=103), level 67
  moves: [psychic, solar-beam, headbutt, egg-bomb]
  Note: solar-beam=76, egg-bomb=121
```

---

## Tier 4 — Élite (AI: hard, level 80-100, 6 Pokémon)

### Entrenador 15: Ghost Master Agatha
```
Pokemon 1: Gengar (id=94), level 80
  moves: [shadow-ball, sludge-bomb, psychic, hyper-beam]
Pokemon 2: Haunter (id=93), level 80
  moves: [shadow-ball, night-shade, lick, sludge-bomb]
  Note: lick=122
Pokemon 3: Arbok (id=24), level 82
  moves: [sludge-bomb, bite, wrap, earthquake]
Pokemon 4: Haunter (id=93), level 83
  moves: [shadow-ball, night-shade, psychic, sludge-bomb]
Pokemon 5: Gengar (id=94), level 85
  moves: [shadow-ball, sludge-bomb, psychic, night-shade]
Pokemon 6: Gengar (id=94), level 88
  moves: [shadow-ball, hyper-beam, sludge-bomb, psychic]
```

### Entrenador 16: Steel Master Steven
```
Pokemon 1: Skarmory (id=227), level 82
  moves: [aerial-ace, steel-wing, fly, sand-attack] → sand-attack no power
  moves: [aerial-ace, steel-wing, fly, slash]
  Note: steel-wing=211
Pokemon 2: Magneton (id=82), level 83
  moves: [thunderbolt, thunder, swift, hyper-beam]
Pokemon 3: Aggron (id=306), level 84
  moves: [iron-tail, earthquake, rock-slide, hyper-beam]
Pokemon 4: Steelix (id=208), level 85
  moves: [iron-tail, earthquake, rock-slide, slam]
Pokemon 5: Metagross (id=376), level 88
  moves: [meteor-mash, earthquake, psychic, hyper-beam]
  Note: meteor-mash=309
Pokemon 6: Metagross (id=376), level 90
  moves: [meteor-mash, earthquake, hyper-beam, iron-tail]
```

### Entrenador 17: Speed Demon (Speed-focused team)
```
Pokemon 1: Jolteon (id=135), level 85
  moves: [thunderbolt, thunder, quick-attack, pin-missile]
Pokemon 2: Crobat (id=169), level 84
  moves: [aerial-ace, sludge-bomb, bite, wing-attack]
Pokemon 3: Alakazam (id=65), level 86
  moves: [psychic, psybeam, shadow-ball, hyper-beam]
Pokemon 4: Starmie (id=121), level 86
  moves: [surf, psychic, ice-beam, thunderbolt]
Pokemon 5: Aerodactyl (id=142), level 87
  moves: [aerial-ace, rock-slide, bite, hyper-beam]
Pokemon 6: Sceptile (id=254), level 88
  moves: [leaf-blade, aerial-ace, iron-tail, quick-attack]
  Note: leaf-blade=348
```

### Entrenador 18: Champion Red (Balanced máximo)
```
Pokemon 1: Pikachu (id=25), level 88
  moves: [thunderbolt, thunder, quick-attack, iron-tail]
Pokemon 2: Espeon (id=196), level 90
  moves: [psychic, psybeam, shadow-ball, hyper-beam]
Pokemon 3: Snorlax (id=143), level 90
  moves: [body-slam, hyper-beam, earthquake, blizzard]
Pokemon 4: Venusaur (id=3), level 92
  moves: [solar-beam, razor-leaf, earthquake, sludge-bomb]
  Note: razor-leaf=75
Pokemon 5: Blastoise (id=9), level 92
  moves: [surf, hydro-pump, ice-beam, earthquake]
  Note: hydro-pump=56
Pokemon 6: Charizard (id=6), level 94
  moves: [flamethrower, fire-blast, aerial-ace, hyper-beam]
```

### Entrenador 19: Legendary Trainer (Extra dificultad)
```
Pokemon 1: Salamence (id=373), level 92
  moves: [dragon-claw, aerial-ace, flamethrower, hyper-beam]
  Note: dragon-claw=337
Pokemon 2: Metagross (id=376), level 92
  moves: [meteor-mash, earthquake, psychic, iron-tail]
Pokemon 3: Gardevoir (id=282), level 93
  moves: [psychic, shadow-ball, calm-mind, thunderbolt] → calm-mind no power
  moves: [psychic, shadow-ball, hyper-beam, thunderbolt]
Pokemon 4: Tyranitar (id=248), level 93
  moves: [crunch, rock-slide, earthquake, hyper-beam]
  Note: crunch=242
Pokemon 5: Swampert (id=260), level 94
  moves: [surf, earthquake, ice-beam, hammer-arm]
  Note: hammer-arm=359
Pokemon 6: Blaziken (id=257), level 95
  moves: [sky-uppercut, blaze-kick, rock-slide, aerial-ace]
  Note: sky-uppercut=327, blaze-kick=299
```

### Entrenador 20: Master Trainer (Jefe Final)
```
Pokemon 1: Rayquaza (id=384), level 98
  moves: [outrage, aerial-ace, hyper-beam, dragon-claw]
  Note: outrage=200
Pokemon 2: Mewtwo (id=150), level 98
  moves: [psychic, shadow-ball, hyper-beam, thunder]
Pokemon 3: Lugia (id=249), level 97
  moves: [aeroblast, psychic, hyper-beam, surf]
  Note: aeroblast=177
Pokemon 4: Ho-Oh (id=250), level 97
  moves: [sacred-fire, solar-beam, hyper-beam, earthquake]
  Note: sacred-fire=221
Pokemon 5: Kyogre (id=382), level 98
  moves: [surf, hydro-pump, ice-beam, thunder]
Pokemon 6: Groudon (id=383), level 98
  moves: [earthquake, rock-slide, fire-blast, hyper-beam]
```

---

## Implementación

### TrainerSeedData.kt (en shared)
```kotlin
object TrainerSeedData {
    val trainers: List<TrainerSeedEntry> = listOf(
        TrainerSeedEntry(
            id = 1,
            name = "Youngster Joey",
            difficultyTier = 1,
            aiStrategy = "easy",
            team = listOf(
                TrainerMemberSeed(pokemonId = 19, level = 25, moveNames = listOf("tackle", "quick-attack", "hyper-fang", "bite")),
                TrainerMemberSeed(pokemonId = 23, level = 22, moveNames = listOf("poison-sting", "bite", "wrap", "acid")),
                TrainerMemberSeed(pokemonId = 21, level = 24, moveNames = listOf("peck", "fury-attack", "aerial-ace", "take-down"))
            )
        ),
        // ... resto de entrenadores
    )
}

data class TrainerSeedEntry(
    val id: Int,
    val name: String,
    val difficultyTier: Int,
    val aiStrategy: String,
    val team: List<TrainerMemberSeed>
)

data class TrainerMemberSeed(
    val pokemonId: Int,
    val level: Int,
    val moveNames: List<String>
)
```

### Serialización a JSON (para TrainerEntity.teamJson)
Serializar `List<TrainerMemberSeed>` a JSON con kotlinx.serialization al insertar en DB.

### Resolución de moves en batalla
Al iniciar batalla, para cada trainer member:
```kotlin
val resolvedMoves = moveNames.mapNotNull { name -> moveDao.getByName(name) }
```
Si quedan 0 moves (todos filtrados), el Pokémon usará Struggle (power=50, type=Normal, category=physical — hardcodeado en engine).
