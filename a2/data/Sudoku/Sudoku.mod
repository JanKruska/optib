param size integer > 0;
set fixedFields within {1..size,1..size};
param fixedValues {fixedFields} integer default 0;

var board {1..size, 1..size} integer, in 1..size;
# board[i,j] = the number assigned to the cell in row i, col j

var numberPoolRows {1..size, 1..size} binary,
# numberPoolRows[i,j] = 1 if row i contains j

#subj to linkFixedFields {i in 1..size, j in 1..size: fixedValues[i,j] > 0}:
subj to linkFixedFields {(i,j) in fixedFields}:
        board[i,j] = fixedValues[i,j];
    
subject to Rows {i in 1..size, j in 1..size, k in 1..size}:
    board[i,k] <= board[j,k];

        subject to Rows {i in 1..size, j in 1..size, k in 1..size}:
        board[i,k] <= board[j,k];
    # cells in the same row must be assigned distinct numbers
 
subj to Cols {j in 1..size}:
    alldiff {i in 1..size} board[i,j];
    # cells in the same column must be assigned distinct numbers
 
subj to Regions {I in 1..size by sqrt(size), J in 1..size by sqrt(size)}:
    # by ensures that I is divisible by sqrt(size) which should be integer
    alldiff {i in I..I+sqrt(size)-1, j in J..J+sqrt(size)-1} board[i,j]; # -1 because of one off condition, example size = 9
    # cells in the same region must be assigned distinct numbers
