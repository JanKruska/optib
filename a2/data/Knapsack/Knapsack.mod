 
#float weigth;
#float value;

param capacity integer;
#param items;
#param weight;
set items;
param value {items} integer;
param weight {items} integer;

var x{items} integer >=0;

maximize used: 
    sum{i in items} x[i]*value[i];

subject to knapsack: 
    sum{i in items} (weight[i]*x[i]) <= capacity;
    
#    forall{i in items} x[i]>=0;
#subject to p: forall{i in items}(x[i]<=capacity);
