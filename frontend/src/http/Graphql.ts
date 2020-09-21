import Mutation from "../model/Mutation";

export default class Graphql {
    static run(query: string, params?: any): Promise<any> {
        const formData = new FormData();
        formData.append("query", query);
        if (params != null) {
            formData.append("variables", JSON.stringify(params));
        }

        return fetch("http://localhost:8080/graphql", {
            method: "post",
            body: formData
        })
            .then(data => data.json())
            .then((response: any) => {
                if (response.errors || !response.data)
                    throw response.errors;
                else
                    return response.data;
            });
    }

    static mutation(query: string, params?: any): Promise<Mutation> {
        return Graphql.run(query, params) as Promise<Mutation>;
    }
}
