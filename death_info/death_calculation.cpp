#include <stdio.h>
#include <stdlib.h>
#include <vector>
#include <map>
#include <iostream>
#include <utility>
#include <fstream>
using namespace std;

typedef map<int, int> causeMap;
typedef map<int, causeMap> regionMap;

int main ()
{

  int year,county,cause,sex,age_code,N;
  FILE * myfile[5];
    myfile[0] = fopen("dead97.txt","r");
     myfile[1] = fopen("dead98.txt","r");
      myfile[2] = fopen("dead99.txt","r");
       myfile[3] = fopen("dead100.txt","r");
        myfile[4] = fopen("dead101.txt","r");

    regionMap reg;
    regionMap::iterator it;
    causeMap ::iterator ij;
    int i;
    for(i=0;i<5;i++)
    {

  while(fscanf(myfile[i],"%d,%d,%d,%d,%d,%d",&year,&county,&cause,&sex,&age_code,&N)!=EOF)
  {
    if(cause!=21&&cause!=22&&cause!=23&&cause!=24&&cause!=25&&cause!=32)
        continue;

    printf("%d,%d,%d,%d,%d,%d\n",year,county,cause,sex,age_code,N);

    county/=100;

     it = reg.find(county);
    if( it== reg.end())
    {
        reg.insert(make_pair(county,causeMap ()));
    }else
    {
        ij = reg[county].find(cause);
        if(ij == reg[county].end())
        {
            reg[county].insert(make_pair(cause,1));
        }
        else
        {
            reg[county][cause] ++;
        }
    }
  }
 fclose(myfile[i]);

  }
    ofstream output;
    output.open("output.txt");

  causeMap ::iterator inner_it;
    for ( it=reg.begin() ; it != reg.end(); it++ ) {
    output << "\ncounty\n" << (*it).first << endl;
    for( inner_it=(*it).second.begin(); inner_it != (*it).second.end(); inner_it++)
      output << (*inner_it).first << " => " << (*inner_it).second << endl;
  }
    output.close();
  return 0;
}
