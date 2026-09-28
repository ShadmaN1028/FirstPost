package com.shadman.firstpost.data

// Invented people and content. None of it is taken from the real app.
object SyntheticData {

    // Timestamps are relative to app launch, so "2h ago" is always true.
    private val launchedAt = System.currentTimeMillis()
    private fun minutesAgo(minutes: Int) = launchedAt - minutes * 60_000L

    val users: List<User> = listOf(
        User("u1", "Maya Chen", "mayachen", 0xFF3B5BDB),
        User("u2", "Tariq Rahman", "tariq.r", 0xFF0B7285),
        User("u3", "Lena Fischer", "lenafi", 0xFF862E9C),
        User("u4", "Arjun Mehta", "arjun_m", 0xFFD9480F),
        User("u5", "Sofia Alvarez", "sofialvz", 0xFF2B8A3E),
        User("u6", "Kwame Owusu", "kwame.o", 0xFFC2255C),
        User("u7", "Hana Sato", "hanasato", 0xFF5F3DC4),
        User("u8", "Daniel Brooks", "dbrooks", 0xFF1864AB),
    )

    val posts: List<Post> = listOf(
        Post(
            id = "p1",
            authorId = "u2",
            text = "Finally finished the bookshelf I've been building for three weekends. " +
                "Crooked? Slightly. Proud? Very.",
            createdAtMillis = minutesAgo(39),
            reactions = reactions("u1" to ReactionType.LIKE, "u4" to ReactionType.LOVE,
                "u5" to ReactionType.LIKE, "u8" to ReactionType.WOW),
            comments = listOf(
                Comment("c1", "u4", "Crooked adds character 😄", minutesAgo(30)),
                Comment("c2", "u1", "Looks great, send pics of it filled up!", minutesAgo(22)),
            ),
            viewCount = 214,
        ),
        Post(
            id = "p2",
            authorId = "u3",
            text = "Morning run by the river. 6 km and the sun came out halfway through ☀️",
            createdAtMillis = minutesAgo(95),
            reactions = reactions("u6" to ReactionType.LIKE, "u7" to ReactionType.LIKE,
                "u2" to ReactionType.LOVE),
            comments = listOf(Comment("c3", "u7", "Best way to start the day", minutesAgo(80))),
            viewCount = 132,
        ),
        Post(
            id = "p3",
            authorId = "u5",
            text = "Anyone have a good recipe for vegetarian lasagne? Cooking for eight on Saturday.",
            createdAtMillis = minutesAgo(160),
            reactions = reactions("u3" to ReactionType.LIKE, "u8" to ReactionType.LIKE),
            comments = listOf(
                Comment("c4", "u1", "Spinach and ricotta, never fails", minutesAgo(150)),
                Comment("c5", "u6", "Roast the veg first, trust me", minutesAgo(140)),
                Comment("c6", "u3", "Following for the answers 👀", minutesAgo(120)),
            ),
            viewCount = 301,
        ),
        Post(
            id = "p4",
            authorId = "u6",
            text = "First day at the new job done. Everyone was lovely, and I only got lost twice.",
            createdAtMillis = minutesAgo(300),
            reactions = reactions("u1" to ReactionType.LOVE, "u2" to ReactionType.LIKE,
                "u3" to ReactionType.LOVE, "u5" to ReactionType.LIKE, "u7" to ReactionType.HAHA),
            comments = listOf(Comment("c7", "u2", "Congrats Kwame! 🎉", minutesAgo(290))),
            viewCount = 458,
        ),
        Post(
            id = "p5",
            authorId = "u7",
            text = "Rainy Sunday, a new puzzle and way too much tea. 1000 pieces, wish me luck.",
            createdAtMillis = minutesAgo(540),
            reactions = reactions("u4" to ReactionType.LIKE, "u8" to ReactionType.HAHA),
            viewCount = 96,
        ),
        Post(
            id = "p6",
            authorId = "u8",
            text = "Our team won the Thursday quiz night for the first time ever 🏆",
            createdAtMillis = minutesAgo(1_380),
            reactions = reactions("u1" to ReactionType.WOW, "u2" to ReactionType.LIKE,
                "u5" to ReactionType.LIKE, "u6" to ReactionType.LOVE),
            comments = listOf(Comment("c8", "u5", "About time!", minutesAgo(1_300))),
            viewCount = 512,
        ),
    )

    // Invented people suggested on "Build your circle" — separate from the
    // main feed cast above so the two lists can evolve independently.
    val circleCandidates: List<User> = listOf(
        User("c1", "Priya Nair", "priyan", 0xFF2B8A3E),
        User("c2", "Owen Baxter", "owenb", 0xFFD9480F),
        User("c3", "Yuki Tanaka", "yukit", 0xFF862E9C),
        User("c4", "Ines Duarte", "inesd", 0xFF0B7285),
        User("c5", "Marcus Webb", "marcusw", 0xFFC2255C),
    )

    // Shown in the feed's "New members" section, alongside the current
    // user's own first post (if they made one). Not part of `posts` above —
    // these are decorative, not part of the reaction/comment system.
    val newMemberPosts: List<Post> = listOf(
        Post(
            id = "new_member_1",
            authorId = "u5",
            text = "Hi everyone! I'm Sofia, just joined VMP 👋",
            createdAtMillis = minutesAgo(4),
            isFirstPost = true,
            viewCount = 12,
        ),
        Post(
            id = "new_member_2",
            authorId = "u7",
            text = "New here! Excited to meet you all 🙌",
            createdAtMillis = minutesAgo(11),
            isFirstPost = true,
            viewCount = 8,
        ),
    )

    val stories: List<Story> = listOf(
        Story("s1", "u1", 0xFF364FC7, "Weekend market finds"),
        Story("s2", "u3", 0xFF7048E8, "River run"),
        Story("s3", "u4", 0xFFE8590C, "Chai o'clock"),
        Story("s4", "u5", 0xFF2F9E44, "Garden update"),
        Story("s5", "u7", 0xFFC2255C, "Puzzle progress"),
    )

    private fun reactions(vararg pairs: Pair<String, ReactionType>): List<Reaction> =
        pairs.map { (userId, type) -> Reaction(userId, type) }
}
