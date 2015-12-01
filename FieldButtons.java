import java.util.*;
import java.io.*;
import java.awt.*;
import java.util.Scanner;
import javax.swing.*;
import java.awt.event.*;

public class FieldButtons {
	public static JButton[][] fieldButton = new JButton[10][10];
	public static JButton[][] opp_fieldButton = new JButton[10][10];
	public static JButton[][] baseButton = new JButton[2][2];
	public static JButton[] buildButton = new JButton[19];
	public static JButton[] troopButton = new JButton[19];
	public static int fieldStates[][] = new int[10][10];
	public static int opp_fieldStates[][] = new int[10][10];
	public static JPanel field_panel = new JPanel();
	public static JPanel opp_field_panel = new JPanel();
	public static JPanel chooser_panel = new JPanel();
	public static JPanel troops_chooser_panel = new JPanel();
	public static JPanel selBase_panel = new JPanel();
	public static ArrayList<ImageIcon> images = new ArrayList<ImageIcon>();
	public static ArrayList<ImageIcon> bases = new ArrayList<ImageIcon>();
	public static int chooserIndex;
	public static int sprite_counter = 0;
	public static int[] countButton = new int[5]; //newly added
	public static int buttonsFlag = 0;

	public static void CreateInitialState(){

		for(int a = 0; a < 10; a++){
			for(int b = 0; b < 10; b++){
				fieldStates[a][b] = 0;
			}
		}

	}
	
	public static void CreatePlayerState(int[][] playerStates){

		fieldStates = playerStates;

	}

	public static void CreateFieldButton(){
		int i = 0, j = 0;

		for(i = 0; i < 10; i++){
			for(j = 0; j < 10; j++){
				int k = fieldStates[i][j];
				fieldButton[i][j] = new JButton(images.get(k));
				field_panel.add(fieldButton[i][j]);

				if(chooserIndex==-1){

				}

				else{
					fieldButton[i][j].addActionListener(new ActionListener(){ // This is for the action listener of each button
						public void actionPerformed(ActionEvent h) {					
							int a = 0, b = 0;
							int fieldIndex1 = 0, fieldIndex2 = 0;
							Object selected;
							selected = h.getSource();
						
							for(a = 0; a < 10; a++){
								for(b = 0; b < 10; b++){
									if(fieldButton[a][b] == selected) {
										fieldIndex1 = a;
										fieldIndex2 = b;	
									}
								}
							}

							if(buttonsFlag==1){ //newly added

							} //newly added

							else{ //newly added

								if(fieldStates[fieldIndex1][fieldIndex2] == 0){
									fieldStates[fieldIndex1][fieldIndex2] = chooserIndex+1;
									fieldButton[fieldIndex1][fieldIndex2].setIcon(images.get(chooserIndex+1));
									chooserIndex = -1;
								}

								else{
									if(chooserIndex == -1){

										if(fieldStates[fieldIndex1][fieldIndex2] == 4){ //newly added
											buildButton[4].setEnabled(true);
										}

										int value = (fieldStates[fieldIndex1][fieldIndex2]-1); //newly added
										countButton[value] = (countButton[value]-1); //newly added
										buildButton[value].setEnabled(true); // newly added

										fieldStates[fieldIndex1][fieldIndex2] = chooserIndex+1;
										fieldButton[fieldIndex1][fieldIndex2].setIcon(images.get(0));
									}
									else{

									}
								}

							} //newly added

						}

					});
				}
			}
		}
	}

	public static void CreateOppFieldButton(){
		int i = 0, j = 0;

		for(i = 0; i < 10; i++){
			for(j = 0; j < 10; j++){
				int k = fieldStates[i][j];
				opp_fieldButton[i][j] = new JButton(images.get(k));
				opp_field_panel.add(opp_fieldButton[i][j]);

				if(chooserIndex==-1){

				}

				else{
					opp_fieldButton[i][j].addActionListener(new ActionListener(){ // This is for the action listener of each button
						public void actionPerformed(ActionEvent h) {					
							int a = 0, b = 0;
							int fieldIndex1 = 0, fieldIndex2 = 0;
							Object selected;
							selected = h.getSource();
						
							for(a = 0; a < 10; a++){
								for(b = 0; b < 10; b++){
									if(opp_fieldButton[a][b] == selected) {
										fieldIndex1 = a;
										fieldIndex2 = b;	
									}
								}
							}

							if(opp_fieldStates[fieldIndex1][fieldIndex2] == 0){
								opp_fieldStates[fieldIndex1][fieldIndex2] = chooserIndex+1;
								opp_fieldButton[fieldIndex1][fieldIndex2].setIcon(images.get(chooserIndex+1));
								chooserIndex = -1;
							}

							else{
								if(chooserIndex == -1){
									opp_fieldStates[fieldIndex1][fieldIndex2] = chooserIndex+1;
									opp_fieldButton[fieldIndex1][fieldIndex2].setIcon(images.get(0));
								}
								else{

								}
							}

						}

					});
				}
			}
		}
	}

	public static void CreateBuildButton(){		
		int i = 0;
		chooserIndex = -1;

		for(i = 0; i < 5; i++){
			buildButton[i] = new JButton(images.get(i+1));
			chooser_panel.add(buildButton[i]);
			
			buildButton[i].addActionListener(new ActionListener() { // This is for the action listener of each button
				public void actionPerformed(ActionEvent h){
					int a = 0;
					Object selected;
					selected = h.getSource();

					if(buttonsFlag==1){ // newly added
						
					} //newly added

					else{ //newly added
						
						for(a = 0; a < 5; a++){
							
							if(buildButton[a] == selected) {

								countButton[a] = (countButton[a]+1); //newly added
								if(countButton[a]==2){				//newly added
									buildButton[a].setEnabled(false); //newly added
								} 										//newly added		

								chooserIndex = a;

								if(chooserIndex == 4){  //newly added
									buildButton[4].setEnabled(false); //newly added
								}
									
							}
						}
					} //newly added
				}
			});

		}
	}

	public static void CreateTroopButton(){		
		int i = 11;
		chooserIndex = -1;

		for(i = 11; i < 18; i++){
			troopButton[i] = new JButton(images.get(i+1));
			troops_chooser_panel.add(troopButton[i]);
			
			troopButton[i].addActionListener(new ActionListener() { // This is for the action listener of each button
				public void actionPerformed(ActionEvent h){
					int a = 0;
					Object selected;
					selected = h.getSource();
						
					for(a = 11; a < 18; a++){
							
						if(troopButton[a] == selected) {
							chooserIndex = a;
									
						}
					}
				}
			});

		}
	}

	public static void UpdateState(String state[]){
		int c = 0;
		
		for(int a = 0; a < 10; a++){
			for(int b = 0; b < 10; b++){
				fieldStates[a][b] = Integer.parseInt(state[c]);
				c++;
			}
		}

		for(int a = 0; a < 10; a++){
			for(int b = 0; b < 10; b++){
				System.out.print(fieldStates[a][b]);
				System.out.print(" ");
			}
			System.out.print("\n");
		}

		CreateFieldButton();
		CreateBuildButton();
	}

	//read the .txt files of list of buildings, and troops
	public static void ImageList(String s){
		try{
			File read_file = new File(s);
			Scanner sc = new Scanner(read_file);

			while(sc.hasNextLine()){
				String line = sc.nextLine();
				images.add(new ImageIcon(line));
			}
		}

		catch(Exception e){

		}
	}

	public static void BaseList(){
		bases.add(new ImageIcon("bases/base1.png"));
		bases.add(new ImageIcon("bases/base2.png"));
		bases.add(new ImageIcon("bases/base3.png"));
		bases.add(new ImageIcon("bases/base4.png"));
	}
}
