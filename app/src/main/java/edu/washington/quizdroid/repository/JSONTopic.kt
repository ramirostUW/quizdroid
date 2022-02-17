package edu.washington.quizdroid.repository

class JSONTopic  (var f1_title: String, var f2_desc: String, var f3_questions: List<JSONQuiz>)  {
    constructor(regTopic: Topic) :this(regTopic.title,
        regTopic.longDescription, JSONQuiz.listJSONQuestions(regTopic.questions))
    companion object {
        fun listJSONTopics(regTopics: List<Topic>): List<JSONTopic>{
            val returnVal = emptyList<JSONTopic>().toMutableList()
            for(topic in regTopics)
            {
                returnVal.add(JSONTopic(topic.title, topic.longDescription, JSONQuiz.listJSONQuestions(topic.questions)))
            }
            return returnVal
        }
    }
    class JSONQuiz (var f1_question : String, var f3_answers : List <String>, var f2_correctAnswer : Int) {
        companion object {
            fun listJSONQuestions(regQuestions:List<Quiz>):List<JSONQuiz>{
                val returnVal = emptyList<JSONQuiz>().toMutableList()
                for(question in regQuestions)
                {
                    returnVal.add(JSONQuiz(question.question, question.answers, question.correctAnswer))
                }
                return returnVal
            }
        }
    }

}