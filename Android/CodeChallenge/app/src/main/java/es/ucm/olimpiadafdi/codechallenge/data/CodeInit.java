package es.ucm.olimpiadafdi.codechallenge.data;

import java.util.ArrayList;

public class CodeInit {

    public static Code initCode(int index){
        ArrayList<CodeLine> code = new ArrayList<CodeLine>();
        int totalErrors;

        switch (index){
            case 0: sample0(code);
                break;
            case 1: sample1(code);
                break;
        }

        totalErrors = 0;
        for( int i = 0; i<code.size(); i++){
            if (code.get(i).getIsError())
                totalErrors++;
        }

        return new Code(code, totalErrors);
    }

    public static void sample0(ArrayList<CodeLine> code){  //Código Selección directa
        code.add(new CodeLine("/* Selección directa*/"));
        code.add(new CodeLine("void ordenaSel (int v[], int num) {"));
        code.add(new CodeLine("	int i, j, menor, aux;"));
        code.add(new CodeLine("	for (i=0; i<num; i--){",
                "	for (i=0; i<num; i++){"));
        code.add(new CodeLine("		menor = 0;",
                "		menor = i;"));
        code.add(new CodeLine("		for (j=i+1; j<num; j++)"));
        code.add(new CodeLine("			if (v[j] < v[menor])"));
        code.add(new CodeLine("				menor = j;",
                "				menor = i;"));
        code.add(new CodeLine("		if (i != menor){"));
        code.add(new CodeLine("		v[i] = aux;",
                "		aux = v[i];"));
        code.add(new CodeLine("		v[i] = v[menor];"));
        code.add(new CodeLine("		v[menor] = aux;"));
        code.add(new CodeLine("		}"));
        code.add(new CodeLine("	}"));
        code.add(new CodeLine("}"));
    }

    public static void sample1(ArrayList<CodeLine> code){  //Código Burbuja
        code.add(new CodeLine("/* Burbuja */"));
        code.add(new CodeLine("void ordenaBur (int v[], int num) {"));
        code.add(new CodeLine("	int i, j, aux;"));
        code.add(new CodeLine("	bool modificado;"));
        code.add(new CodeLine("	i = num;",
                "	i = 0;"));
        code.add(new CodeLine("	modificado = false;",
                "	modificado = true;"));
        code.add(new CodeLine("	while ((i < num - 1) && modificado) {"));
        code.add(new CodeLine("		modificado = false;"));
        code.add(new CodeLine("		for (j = num-1; j > i; j++)",
                "		for (j = num-1; j > i; j--)"));
        code.add(new CodeLine("			if (v[j] < v[j-1]){"));
        code.add(new CodeLine("				aux = v[j];"));
        code.add(new CodeLine("				v[j] = v[j-1];"));
        code.add(new CodeLine("				v[j-1] = aux;"));
        code.add(new CodeLine("				modificado = true;"));
        code.add(new CodeLine("			}"));
        code.add(new CodeLine("		i--",
                "		i++"));
        code.add(new CodeLine("	}"));
        code.add(new CodeLine("}"));
    }
}
