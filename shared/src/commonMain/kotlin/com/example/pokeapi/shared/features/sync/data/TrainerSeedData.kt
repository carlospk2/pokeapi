package com.example.pokeapi.shared.features.sync.data

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

object TrainerSeedData {
    val trainers: List<TrainerSeedEntry> = listOf(
        // Tier 1 - Novato (easy, level 20-30, 3 pokemon)
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
        TrainerSeedEntry(
            id = 2,
            name = "Lass Lily",
            difficultyTier = 1,
            aiStrategy = "easy",
            team = listOf(
                TrainerMemberSeed(pokemonId = 39, level = 20, moveNames = listOf("pound", "body-slam", "double-slap", "headbutt")),
                TrainerMemberSeed(pokemonId = 35, level = 22, moveNames = listOf("pound", "double-slap", "headbutt", "body-slam")),
                TrainerMemberSeed(pokemonId = 52, level = 25, moveNames = listOf("scratch", "bite", "pay-day", "headbutt"))
            )
        ),
        TrainerSeedEntry(
            id = 3,
            name = "Bug Catcher Billy",
            difficultyTier = 1,
            aiStrategy = "easy",
            team = listOf(
                TrainerMemberSeed(pokemonId = 15, level = 20, moveNames = listOf("twineedle", "poison-sting", "fury-attack", "aerial-ace")),
                TrainerMemberSeed(pokemonId = 12, level = 22, moveNames = listOf("confusion", "gust", "tackle", "aerial-ace")),
                TrainerMemberSeed(pokemonId = 47, level = 25, moveNames = listOf("scratch", "fury-cutter", "slash", "bug-bite"))
            )
        ),
        TrainerSeedEntry(
            id = 4,
            name = "Hiker Rocky",
            difficultyTier = 1,
            aiStrategy = "easy",
            team = listOf(
                TrainerMemberSeed(pokemonId = 74, level = 28, moveNames = listOf("tackle", "rock-throw", "rollout", "rock-blast")),
                TrainerMemberSeed(pokemonId = 95, level = 28, moveNames = listOf("tackle", "rock-throw", "bind", "slam")),
                TrainerMemberSeed(pokemonId = 66, level = 30, moveNames = listOf("low-kick", "karate-chop", "seismic-toss", "rock-throw"))
            )
        ),
        // Tier 2 - Intermedio (medium, level 40-50, 4-5 pokemon)
        TrainerSeedEntry(
            id = 5,
            name = "Ace Trainer Blaze",
            difficultyTier = 2,
            aiStrategy = "medium",
            team = listOf(
                TrainerMemberSeed(pokemonId = 59, level = 45, moveNames = listOf("flamethrower", "bite", "extremespeed", "iron-tail")),
                TrainerMemberSeed(pokemonId = 78, level = 44, moveNames = listOf("flamethrower", "fire-spin", "stomp", "take-down")),
                TrainerMemberSeed(pokemonId = 126, level = 45, moveNames = listOf("flamethrower", "fire-punch", "thunder-punch", "body-slam")),
                TrainerMemberSeed(pokemonId = 6, level = 48, moveNames = listOf("flamethrower", "fire-blast", "aerial-ace", "slash"))
            )
        ),
        TrainerSeedEntry(
            id = 6,
            name = "Swimmer Marina",
            difficultyTier = 2,
            aiStrategy = "medium",
            team = listOf(
                TrainerMemberSeed(pokemonId = 134, level = 43, moveNames = listOf("surf", "water-gun", "bite", "quick-attack")),
                TrainerMemberSeed(pokemonId = 117, level = 44, moveNames = listOf("surf", "bubble-beam", "twister", "water-gun")),
                TrainerMemberSeed(pokemonId = 91, level = 45, moveNames = listOf("surf", "ice-beam", "icicle-spear", "spike-cannon")),
                TrainerMemberSeed(pokemonId = 130, level = 48, moveNames = listOf("surf", "hyper-beam", "bite", "dragon-rage"))
            )
        ),
        TrainerSeedEntry(
            id = 7,
            name = "Psychic Sabrina",
            difficultyTier = 2,
            aiStrategy = "medium",
            team = listOf(
                TrainerMemberSeed(pokemonId = 64, level = 43, moveNames = listOf("psychic", "psybeam", "headbutt", "confusion")),
                TrainerMemberSeed(pokemonId = 122, level = 44, moveNames = listOf("psychic", "psybeam", "body-slam", "headbutt")),
                TrainerMemberSeed(pokemonId = 124, level = 45, moveNames = listOf("psychic", "ice-beam", "blizzard", "pound")),
                TrainerMemberSeed(pokemonId = 65, level = 48, moveNames = listOf("psychic", "psybeam", "headbutt", "body-slam"))
            )
        ),
        TrainerSeedEntry(
            id = 8,
            name = "Black Belt Koga",
            difficultyTier = 2,
            aiStrategy = "medium",
            team = listOf(
                TrainerMemberSeed(pokemonId = 67, level = 42, moveNames = listOf("karate-chop", "low-kick", "cross-chop", "rock-throw")),
                TrainerMemberSeed(pokemonId = 57, level = 44, moveNames = listOf("karate-chop", "low-kick", "cross-chop", "thrash")),
                TrainerMemberSeed(pokemonId = 106, level = 46, moveNames = listOf("high-jump-kick", "mega-kick", "jump-kick", "brick-break")),
                TrainerMemberSeed(pokemonId = 107, level = 46, moveNames = listOf("ice-punch", "fire-punch", "thunder-punch", "brick-break")),
                TrainerMemberSeed(pokemonId = 68, level = 48, moveNames = listOf("cross-chop", "karate-chop", "earthquake", "rock-slide"))
            )
        ),
        TrainerSeedEntry(
            id = 9,
            name = "Ace Trainer Elena",
            difficultyTier = 2,
            aiStrategy = "medium",
            team = listOf(
                TrainerMemberSeed(pokemonId = 135, level = 44, moveNames = listOf("thunderbolt", "quick-attack", "pin-missile", "bite")),
                TrainerMemberSeed(pokemonId = 101, level = 44, moveNames = listOf("thunderbolt", "spark", "rollout", "swift")),
                TrainerMemberSeed(pokemonId = 82, level = 45, moveNames = listOf("thunderbolt", "swift", "thunder", "headbutt")),
                TrainerMemberSeed(pokemonId = 26, level = 47, moveNames = listOf("thunderbolt", "thunder", "quick-attack", "iron-tail"))
            )
        ),
        // Tier 3 - Avanzado (medium, level 60-70, 6 pokemon)
        TrainerSeedEntry(
            id = 10,
            name = "Dragon Tamer Lance",
            difficultyTier = 3,
            aiStrategy = "medium",
            team = listOf(
                TrainerMemberSeed(pokemonId = 148, level = 62, moveNames = listOf("dragon-rage", "slam", "body-slam", "ice-beam")),
                TrainerMemberSeed(pokemonId = 148, level = 63, moveNames = listOf("dragon-rage", "slam", "body-slam", "fire-blast")),
                TrainerMemberSeed(pokemonId = 142, level = 64, moveNames = listOf("bite", "aerial-ace", "rock-slide", "hyper-beam")),
                TrainerMemberSeed(pokemonId = 130, level = 65, moveNames = listOf("surf", "hyper-beam", "bite", "dragon-rage")),
                TrainerMemberSeed(pokemonId = 148, level = 65, moveNames = listOf("dragon-rage", "slam", "body-slam", "thunder")),
                TrainerMemberSeed(pokemonId = 149, level = 68, moveNames = listOf("hyper-beam", "fire-blast", "thunder", "blizzard"))
            )
        ),
        TrainerSeedEntry(
            id = 11,
            name = "Ice Queen Lorelei",
            difficultyTier = 3,
            aiStrategy = "medium",
            team = listOf(
                TrainerMemberSeed(pokemonId = 87, level = 62, moveNames = listOf("surf", "ice-beam", "aurora-beam", "headbutt")),
                TrainerMemberSeed(pokemonId = 91, level = 63, moveNames = listOf("surf", "ice-beam", "icicle-spear", "spike-cannon")),
                TrainerMemberSeed(pokemonId = 80, level = 63, moveNames = listOf("surf", "psychic", "ice-beam", "headbutt")),
                TrainerMemberSeed(pokemonId = 124, level = 64, moveNames = listOf("psychic", "ice-beam", "blizzard", "pound")),
                TrainerMemberSeed(pokemonId = 131, level = 65, moveNames = listOf("surf", "ice-beam", "body-slam", "blizzard")),
                TrainerMemberSeed(pokemonId = 91, level = 66, moveNames = listOf("surf", "ice-beam", "blizzard", "spike-cannon"))
            )
        ),
        TrainerSeedEntry(
            id = 12,
            name = "Poison Master Koga",
            difficultyTier = 3,
            aiStrategy = "medium",
            team = listOf(
                TrainerMemberSeed(pokemonId = 89, level = 62, moveNames = listOf("sludge-bomb", "body-slam", "headbutt", "acid")),
                TrainerMemberSeed(pokemonId = 110, level = 63, moveNames = listOf("sludge-bomb", "body-slam", "tackle", "headbutt")),
                TrainerMemberSeed(pokemonId = 49, level = 63, moveNames = listOf("psychic", "sludge-bomb", "aerial-ace", "gust")),
                TrainerMemberSeed(pokemonId = 94, level = 65, moveNames = listOf("shadow-ball", "sludge-bomb", "psychic", "night-shade")),
                TrainerMemberSeed(pokemonId = 24, level = 64, moveNames = listOf("poison-sting", "bite", "wrap", "sludge-bomb")),
                TrainerMemberSeed(pokemonId = 94, level = 67, moveNames = listOf("shadow-ball", "sludge-bomb", "psychic", "hyper-beam"))
            )
        ),
        TrainerSeedEntry(
            id = 13,
            name = "Rock Master Brock",
            difficultyTier = 3,
            aiStrategy = "medium",
            team = listOf(
                TrainerMemberSeed(pokemonId = 75, level = 62, moveNames = listOf("rock-slide", "earthquake", "magnitude", "self-destruct")),
                TrainerMemberSeed(pokemonId = 112, level = 64, moveNames = listOf("earthquake", "rock-slide", "horn-attack", "hyper-beam")),
                TrainerMemberSeed(pokemonId = 139, level = 63, moveNames = listOf("surf", "rock-slide", "spike-cannon", "bite")),
                TrainerMemberSeed(pokemonId = 76, level = 65, moveNames = listOf("earthquake", "rock-slide", "body-slam", "hyper-beam")),
                TrainerMemberSeed(pokemonId = 95, level = 63, moveNames = listOf("rock-slide", "earthquake", "slam", "bind")),
                TrainerMemberSeed(pokemonId = 142, level = 66, moveNames = listOf("rock-slide", "aerial-ace", "bite", "hyper-beam"))
            )
        ),
        TrainerSeedEntry(
            id = 14,
            name = "Ace Bruno",
            difficultyTier = 3,
            aiStrategy = "medium",
            team = listOf(
                TrainerMemberSeed(pokemonId = 123, level = 62, moveNames = listOf("slash", "aerial-ace", "fury-cutter", "wing-attack")),
                TrainerMemberSeed(pokemonId = 127, level = 63, moveNames = listOf("slash", "vice-grip", "seismic-toss", "brick-break")),
                TrainerMemberSeed(pokemonId = 65, level = 64, moveNames = listOf("psychic", "psybeam", "headbutt", "shadow-ball")),
                TrainerMemberSeed(pokemonId = 68, level = 65, moveNames = listOf("cross-chop", "earthquake", "rock-slide", "hyper-beam")),
                TrainerMemberSeed(pokemonId = 94, level = 65, moveNames = listOf("shadow-ball", "sludge-bomb", "psychic", "night-shade")),
                TrainerMemberSeed(pokemonId = 103, level = 67, moveNames = listOf("psychic", "solar-beam", "headbutt", "egg-bomb"))
            )
        ),
        // Tier 4 - Elite (hard, level 80-100, 6 pokemon)
        TrainerSeedEntry(
            id = 15,
            name = "Ghost Master Agatha",
            difficultyTier = 4,
            aiStrategy = "hard",
            team = listOf(
                TrainerMemberSeed(pokemonId = 94, level = 80, moveNames = listOf("shadow-ball", "sludge-bomb", "psychic", "hyper-beam")),
                TrainerMemberSeed(pokemonId = 93, level = 80, moveNames = listOf("shadow-ball", "night-shade", "lick", "sludge-bomb")),
                TrainerMemberSeed(pokemonId = 24, level = 82, moveNames = listOf("sludge-bomb", "bite", "wrap", "earthquake")),
                TrainerMemberSeed(pokemonId = 93, level = 83, moveNames = listOf("shadow-ball", "night-shade", "psychic", "sludge-bomb")),
                TrainerMemberSeed(pokemonId = 94, level = 85, moveNames = listOf("shadow-ball", "sludge-bomb", "psychic", "night-shade")),
                TrainerMemberSeed(pokemonId = 94, level = 88, moveNames = listOf("shadow-ball", "hyper-beam", "sludge-bomb", "psychic"))
            )
        ),
        TrainerSeedEntry(
            id = 16,
            name = "Steel Master Steven",
            difficultyTier = 4,
            aiStrategy = "hard",
            team = listOf(
                TrainerMemberSeed(pokemonId = 227, level = 82, moveNames = listOf("aerial-ace", "steel-wing", "fly", "slash")),
                TrainerMemberSeed(pokemonId = 82, level = 83, moveNames = listOf("thunderbolt", "thunder", "swift", "hyper-beam")),
                TrainerMemberSeed(pokemonId = 306, level = 84, moveNames = listOf("iron-tail", "earthquake", "rock-slide", "hyper-beam")),
                TrainerMemberSeed(pokemonId = 208, level = 85, moveNames = listOf("iron-tail", "earthquake", "rock-slide", "slam")),
                TrainerMemberSeed(pokemonId = 376, level = 88, moveNames = listOf("meteor-mash", "earthquake", "psychic", "hyper-beam")),
                TrainerMemberSeed(pokemonId = 376, level = 90, moveNames = listOf("meteor-mash", "earthquake", "hyper-beam", "iron-tail"))
            )
        ),
        TrainerSeedEntry(
            id = 17,
            name = "Speed Demon",
            difficultyTier = 4,
            aiStrategy = "hard",
            team = listOf(
                TrainerMemberSeed(pokemonId = 135, level = 85, moveNames = listOf("thunderbolt", "thunder", "quick-attack", "pin-missile")),
                TrainerMemberSeed(pokemonId = 169, level = 84, moveNames = listOf("aerial-ace", "sludge-bomb", "bite", "wing-attack")),
                TrainerMemberSeed(pokemonId = 65, level = 86, moveNames = listOf("psychic", "psybeam", "shadow-ball", "hyper-beam")),
                TrainerMemberSeed(pokemonId = 121, level = 86, moveNames = listOf("surf", "psychic", "ice-beam", "thunderbolt")),
                TrainerMemberSeed(pokemonId = 142, level = 87, moveNames = listOf("aerial-ace", "rock-slide", "bite", "hyper-beam")),
                TrainerMemberSeed(pokemonId = 254, level = 88, moveNames = listOf("leaf-blade", "aerial-ace", "iron-tail", "quick-attack"))
            )
        ),
        TrainerSeedEntry(
            id = 18,
            name = "Champion Red",
            difficultyTier = 4,
            aiStrategy = "hard",
            team = listOf(
                TrainerMemberSeed(pokemonId = 25, level = 88, moveNames = listOf("thunderbolt", "thunder", "quick-attack", "iron-tail")),
                TrainerMemberSeed(pokemonId = 196, level = 90, moveNames = listOf("psychic", "psybeam", "shadow-ball", "hyper-beam")),
                TrainerMemberSeed(pokemonId = 143, level = 90, moveNames = listOf("body-slam", "hyper-beam", "earthquake", "blizzard")),
                TrainerMemberSeed(pokemonId = 3, level = 92, moveNames = listOf("solar-beam", "razor-leaf", "earthquake", "sludge-bomb")),
                TrainerMemberSeed(pokemonId = 9, level = 92, moveNames = listOf("surf", "hydro-pump", "ice-beam", "earthquake")),
                TrainerMemberSeed(pokemonId = 6, level = 94, moveNames = listOf("flamethrower", "fire-blast", "aerial-ace", "hyper-beam"))
            )
        ),
        TrainerSeedEntry(
            id = 19,
            name = "Legendary Trainer",
            difficultyTier = 4,
            aiStrategy = "hard",
            team = listOf(
                TrainerMemberSeed(pokemonId = 373, level = 92, moveNames = listOf("dragon-claw", "aerial-ace", "flamethrower", "hyper-beam")),
                TrainerMemberSeed(pokemonId = 376, level = 92, moveNames = listOf("meteor-mash", "earthquake", "psychic", "iron-tail")),
                TrainerMemberSeed(pokemonId = 282, level = 93, moveNames = listOf("psychic", "shadow-ball", "hyper-beam", "thunderbolt")),
                TrainerMemberSeed(pokemonId = 248, level = 93, moveNames = listOf("crunch", "rock-slide", "earthquake", "hyper-beam")),
                TrainerMemberSeed(pokemonId = 260, level = 94, moveNames = listOf("surf", "earthquake", "ice-beam", "hammer-arm")),
                TrainerMemberSeed(pokemonId = 257, level = 95, moveNames = listOf("sky-uppercut", "blaze-kick", "rock-slide", "aerial-ace"))
            )
        ),
        TrainerSeedEntry(
            id = 20,
            name = "Master Trainer",
            difficultyTier = 4,
            aiStrategy = "hard",
            team = listOf(
                TrainerMemberSeed(pokemonId = 384, level = 98, moveNames = listOf("outrage", "aerial-ace", "hyper-beam", "dragon-claw")),
                TrainerMemberSeed(pokemonId = 150, level = 98, moveNames = listOf("psychic", "shadow-ball", "hyper-beam", "thunder")),
                TrainerMemberSeed(pokemonId = 249, level = 97, moveNames = listOf("aeroblast", "psychic", "hyper-beam", "surf")),
                TrainerMemberSeed(pokemonId = 250, level = 97, moveNames = listOf("sacred-fire", "solar-beam", "hyper-beam", "earthquake")),
                TrainerMemberSeed(pokemonId = 382, level = 98, moveNames = listOf("surf", "hydro-pump", "ice-beam", "thunder")),
                TrainerMemberSeed(pokemonId = 383, level = 98, moveNames = listOf("earthquake", "rock-slide", "fire-blast", "hyper-beam"))
            )
        )
    )
}
