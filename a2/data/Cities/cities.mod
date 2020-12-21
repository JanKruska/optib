set cities;
param x {cities} integer;
param y {cities} integer;

var e{cities,cities} binary;

#dvar float z[1..3][1..3];

maximize used: 
    sum{i in cities, j in cities: i>=j} e[i,j]*sqrt((x[i]-x[j])*(x[i]-x[j])+(y[i]-y[j])*(y[i]-y[j]));

subject to row_sum {i in cities}: 
        sum{j in cities} (e[i,j]) = 1;
    
subject to column_sum {i in cities}: 
        sum{j in cities} (e[j,i]) = 1;
 
#subject to self_pair:
#    forall{i in cities} e[i,i] = 0;
