package com.botTelRadar.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.botTelRadar.model.VeiculoInfo;
import com.botTelRadar.service.VeiculoInfoService;
import com.botTelRadar.util.JwtTokenUtil;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    @Value("${telegram.bot.username}")
    private String botUsername;

    @Value("${telegram.bot.token}")
    private String botToken;

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final VeiculoInfoService veiculoInfoService;

    // Mapa de sessões para armazenar o token de cada usuário autenticado (chatId -> token)
    private final Map<String, String> activeSessions = new HashMap<>();

    @Autowired
    public TelegramBot(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil,
                       VeiculoInfoService veiculoInfoService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.veiculoInfoService = veiculoInfoService;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Message message = update.getMessage();
            String chatId = message.getChatId().toString();
            String text = message.getText();

            if (text.startsWith("/login")) {
                handleLoginCommand(chatId, text);
            } else if (text.startsWith("/buscar_veiculo")) {
                handleBuscarVeiculoCommand(chatId, text);
            } else {
                sendMessage(chatId, "Comando não reconhecido. Use /login para autenticar.");
            }
        }
    }

    private void handleLoginCommand(String chatId, String text) {
        String[] credentials = text.split(" ");
        if (credentials.length == 3) {
            String username = credentials[1];
            String password = credentials[2];
            authenticateAndRespond(chatId, username, password);
        } else {
            sendMessage(chatId, "Para fazer login, use: /login <usuário> <senha>");
        }
    }

    private void authenticateAndRespond(String chatId, String username, String password) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtTokenUtil.generateToken(userDetails.getUsername());

            // Armazena o token de sessão associado ao chatId do usuário
            activeSessions.put(chatId, token);

            sendMessage(chatId, "Login realizado com sucesso! Sessão iniciada.");
        } catch (AuthenticationException e) {
            sendMessage(chatId, "Erro ao realizar login: Credenciais inválidas.");
        }
    }

    private void handleBuscarVeiculoCommand(String chatId, String text) {
        if (!activeSessions.containsKey(chatId)) {
            sendMessage(chatId, "Você precisa fazer login primeiro. Use /login <usuário> <senha>.");
            return;
        }

        String[] parts = text.split(" ");
        if (parts.length == 2) {
            String numeroPlaca = parts[1];
            buscarVeiculo(chatId, numeroPlaca);
        } else {
            sendMessage(chatId, "Para buscar um veículo, use: /buscar_veiculo <numero_placa>");
        }
    }

    private void buscarVeiculo(String chatId, String numeroPlaca) {
        var veiculoInfo = veiculoInfoService.findByNumeroPlaca(numeroPlaca);
        if (veiculoInfo.isPresent()) {
            VeiculoInfo veiculo = veiculoInfo.get();
            String formattedMessage = String.format(
                "🔍 *Veículo encontrado:*\n" +
                "• *ID:* %d\n" +
                "• *Placa:* %s\n" +
                "• *Proprietário:* %s\n" +
                "• *CPF/CNPJ:* %s\n" +
                "• *Data do Pedido:* %s\n" +
                "• *Estado:* %s\n" +
                "• *Cidade:* %s\n" +
                "• *Nome do Contato:* %s\n" +
                "• *Contato:* %s\n",
                veiculo.getId(),
                veiculo.getNumeroPlaca(),
                veiculo.getNomeProprietario(),
                veiculo.getCpfCnpj(),
                veiculo.getDataPedido(),
                veiculo.getEstado(),
                veiculo.getCidade(),
                veiculo.getNomeContato(),
                veiculo.getContato()
            );
            
            sendMessage(chatId, formattedMessage);
        } else {
            sendMessage(chatId, "Veículo com placa " + numeroPlaca + " não encontrado.");
        }
    }

    private void sendMessage(String chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}