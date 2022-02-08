package edu.washington.quizdroid.repository

class HardCodedRepository() : TopicRepository {

    private fun initializeRepositoryOnInit() : List<Topic> {
        var physicsQ1 = Quiz("If Force is 6 Newtons, and mass is 3 kg, " +
                "acceleration is _ m/s^2.",
        listOf("one", "two", "three", "four"), 2)
        var physicsQ2 = Quiz("If Force is 9 Newtons, and mass is 3 kg, " +
                "acceleration is _ m/s^2.",
            listOf("one", "two", "three", "four"), 3)
        var physicsQ3 = Quiz("If Force is 12 Newtons, and mass is 3 kg, " +
                "acceleration is _ m/s^2.",
            listOf("one", "two", "three", "four"), 4)

        var mathQ1 = Quiz("What is 1 + 1?",
            listOf("one", "two", "three", "four"), 2)
        var mathQ2 = Quiz("What is the answer to 2 + 1?",
            listOf("one", "two", "three", "four"), 3)
        var mathQ3 = Quiz("Why don't you tell me what is 3 + 1?",
            listOf("one", "two", "three", "four"), 4)

        var marvelQ1 = Quiz("How many Guardians of the Galaxy movies have been released?",
            listOf("one", "two", "three", "four"), 2)
        var marvelQ2 = Quiz("How many Spiderman movies are in the MCU?",
            listOf("one", "two", "three", "four"), 3)
        var marvelQ3 = Quiz("How many Avengers movies have been released?",
            listOf("one", "two", "three", "four"), 4)

        var mathTopic = Topic("Math", "Take our math quiz to see if " +
                "your Math Skills add up!",
            "Take our math quiz to see if your Math Skills add up!",
            listOf<Quiz>(mathQ1, mathQ2, mathQ3))

        var physicsTopic = Topic("Physics", "Take our physics quiz " +
                "to see if your knowledge is a Force to be reckoned with!",
            "Take our physics quiz to see if your knowledge is a " +
                    "Force to be reckoned with!",
            listOf(physicsQ1, physicsQ2, physicsQ3))
        var marvelTopic = Topic("Marvel SuperHeroes",
            "Take our super quiz on superheroes!",
            "Take our super quiz on superheroes!",
            listOf(marvelQ1, marvelQ2, marvelQ3))

        return listOf(mathTopic, marvelTopic, physicsTopic)
    }

    override val topics: List<Topic> = initializeRepositoryOnInit()

    override fun getTopicNames(): List<String>{
        var returnVal = listOf<String>().toMutableList();
        for(topic in topics){
            returnVal.add(topic.title)
        }
        return returnVal;
    }

    override fun getQuestions(topicName: String): List<Quiz>{
        for(topic in topics){
            if(topic.title == topicName)
                return topic.questions;
        }
        return emptyList();
    }

    override fun getTopic(topicName: String): Topic{
        for(topic in topics){
            if(topic.title == topicName)
                return topic;
        }
        throw IllegalArgumentException("No topic found");
    }
}