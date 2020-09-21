//package eu.alkismavridis.bambumbot.infrastructure.api
//
//import com.fasterxml.jackson.databind.JsonNode
//import graphql.language.ObjectField
//import graphql.language.ObjectValue
//import graphql.language.StringValue
//import graphql.schema.Coercing
//
//class JsonScalarCoercing : Coercing<Map<String, Any?>?, ObjectField> {
//    override fun parseValue(input: Any?): Map<String, Any?>? {
//        print("parseValue was called")
//        return null
//    }
//
//    override fun parseLiteral(input: Any?): Map<String, Any?>? {
//        print("parseLiteral was called")
//        val result = mutableMapOf<String, Any?>()
//        (input as ObjectValue).objectFields.forEach {
//            result[it.name] = (it.value as StringValue).value
//        }
//
//        return result
//    }
//
//    override fun serialize(dataFetcherResult: Any?): ObjectField? {
//        print("serialize was called")
//        return null
//    }
//}
