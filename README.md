# A-Star
Code for A* algorithm implementation for Spain map and 2x2x2 RubikCube.

Spain map:
the data for the map is in https://mat.uab.cat/~alseda/MasterOpt/index.html
  Some cities are:
    - 771979683 - Girona
    - 255227829 - Barruera (Oest pirineu català)
    - 429854583 - Lleida
    - 698734221 - Villalba, Lugo
    - 576259137 - Zagra, Granada
    - 654390449 - Soria
    - 534014082 - Peñaflor de Gallego, Zaragoza
    - 296858801 - Manacor del Valle, Mallorca
    - 419779047 - San Martín de Quevedo
    - 145785061 - Mérida
    
  Tests cases:
    	// MAP MULT
        // Double[] percentages = {0., 0.0005, 0.001, 0.002, 0.003, 0.004, 0.005, 0.0075, 0.01,0.02,0.03,0.04,0.05,0.10,0.20,0.30,0.40,0.50,0.75,1.,2.5,5.,10.};
	// MAP SUM
        // Double[] percentages = {5.,25.,50.,75.,100., 250., 500., 750., 1000., 2500., 5000., 10000., 25000.,50000.,75000.,100000.,250000.,500000.,750000.,1000000.};
   	and then
        	// Map.compute(percentages,Node.SUMA,100,graf);
	with Node.SUMA or Node.MULT depending on the case and being the third parameter the number of iterations.
	
    To create a path between two cities with different heuristics storing all of them: (In this case from Girona to Villalba, Lugo)
   	// Map.computeGrafic(771979683, 698734221 ,percentages,Node.MULT,graf);
 

2x2x2 Rubik cube (Pocket cube):
  Test cases:
      // RUBIK SUM
      // Double[] percentages = {0.,0.25,0.5,0.75,1.,1.5,2.,3.,4.,5.,6.,7.,8.,9.,10.,12.5,15.,17.5,20.};
      // RUBI MULT
      // Double[] percentages = {0., 0.0005, 0.001, 0.002, 0.003, 0.004, 0.005, 0.0075, 0.01,0.02,0.03,0.04,0.05,0.10,0.20,0.30,0.40,0.50,0.75,1.,2.5,5.,10.};
	  and then
      // Rubik.compute(percentages,Rubik2Node.MULT,iterations,graf,end,heuristicMap);
    with Rubik2Node.SUMA or Rubik2Node.MULT depending on the case and being the third parameter the number of iterations.
    
 
 
