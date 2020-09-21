# API
We use graphql.

Example:
```
curl -X POST http://localhost:8080/graphql \
    --form 'query={ robots { id, name } }' \
    --form 'variables={"a":"fff"}'
```

The parameters are:
- query (required)
- variables (optional)
- operationName (optional)



# EXAMPLE QUERIES

Create Robot:
```
mutation { createRobot(
  networkConfig:{
		name: "Test",
    activation: "LEAKY_RELU",
    layers: [
      {type:"FULLY_CONNECTED", neuronCount: 2},
      {type:"FULLY_CONNECTED", neuronCount: 3, times: 1},
      {type:"OUT", neuronCount: 1, activation: "LINEAR"}
    ]
  },

  dataProviderConfig: {type: "ADD"},
  trainerConfig: {type: "BATCH"}
  ) { id } }
```
