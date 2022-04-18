import random
from datetime import datetime

n = 100
print(n,n)
for i in range(n):
    num1 = 1 + i
    num2 = num1 + 1
    num3 = random.randint(1,10)
    if(num2> n):
        num2-= n
    print (num1,num2,num3)