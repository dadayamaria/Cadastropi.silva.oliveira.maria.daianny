package com.example.cadastropisilvaoliveiramariadaianny;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainRepositorio {
    Context context;
    public MainRepositorio(Context context) {
        this.context = context;
    }

    /**
     * Método que cria uma requisição HTTP para registrar um novo usuário junto ao servidor web.
     * @param newLogin o login do novo usuário
     * @param newPassword a senha do novo usuário
     * @return true se o usuário foi cadastrado e false caso contrário
     */
    public boolean cadastrar(String novoNome, String novoCpf,String novoEmail, String novoTelefone, String novoDataNasc) {



        String result = "";
        try {

            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date date = formatter.parse(novoDataNasc);

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String requiredDate = df.format(date).toString();

            // Cria uma requisição HTTP a adiona o parâmetros que devem ser enviados ao servidor
            HttpRequest httpRequest = new HttpRequest(Config.CADASTRO_APP_URL + "cadastramento.php", "POST", "UTF-8");
            httpRequest.addParam("nome", novoNome);
            httpRequest.addParam("cpf", novoCpf);
            httpRequest.addParam("email", novoEmail);
            httpRequest.addParam("phone", novoTelefone);
            httpRequest.addParam("d_nasc", requiredDate);

            // Executa a requisição HTTP. É neste momento que o servidor web é contactado. Ao executar
            // a requisição é aberto um fluxo de dados entre o servidor e a app (InputStream is).
            InputStream is = httpRequest.execute();

            // Obtém a resposta fornecida pelo servidor. O InputStream é convertido em uma String. Essa
            // String é a resposta do servidor web em formato JSON.
            //
            // Em caso de sucesso, será retornada uma String JSON no formato:
            //
            // {"sucesso":1}
            //
            // Em caso de falha, será retornada uma String JSON no formato:
            //
            // {"sucesso":0, "erro":"usuario já existe"}
            result = Util.inputStream2String(is, "UTF-8");

            Log.i("HTTP REGISTER RESULT", result);

            // Fecha a conexão com o servidor web.
            httpRequest.finish();

            // A classe JSONObject recebe como parâmetro do construtor uma String no formato JSON e
            // monta internamente uma estrutura de dados similar ao dicionário em python.
            JSONObject jsonObject = new JSONObject(result);

            // obtem o valor da chave sucesso para verificar se a ação ocorreu da forma esperada ou não.
            int success = jsonObject.getInt("sucesso");

            // Se sucesso igual a 1, significa que o usuário foi registrado com sucesso.
            if(success == 1) {
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("HTTP RESULT", result);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return false;

    }