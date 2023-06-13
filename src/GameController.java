import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class GameController {
    private final GameBoard gameBoard = new GameBoard();

    public void start() {
        System.out.println("Zaczyna się gra");
        int amountOfPlayers = decideHowManyPlayers();
        gameBoard.preparPlayers(amountOfPlayers);
        System.out.println("Przygotowano " + amountOfPlayers + " graczy");
        System.out.println("Rozdano każdemu po 5 kart");
        gameBoard.givePlayersStartingCards();
        System.out.println("Położono pierwszą kartą na stos");
        gameBoard.putStartingCardOnStack();
        System.out.println(gameBoard);
        System.out.println("Rozegraj turę");
        playTurn(amountOfPlayers);
        System.out.println(gameBoard);
    }

    private void playTurn(int players) {

        do {
            playerTurn();
            for (int id = 2; id <= players; id++) {
                computerTurn(id);
            }
        }
        while (!isVictoryAchieve());

    }

    private void playerTurn() {
        Player player = gameBoard.getPlayers().get(0);
        int amountOfCards = player.getCards().size();

        System.out.println("///// Tura Gracza " + player.getId() + " /////");
        System.out.println("Karta na stosie: " + gameBoard.getStack().getLast());
        System.out.println("0. Dobierz kartę");
        for (int i = 0; i < player.getCards().size(); i++) {
            System.out.println(i + 1 + ". " + player.getCards().get(i));
        }

        //Akcje gracza:

        Scanner scanner = new Scanner(System.in);
        int playerChoice;
        do {
            System.out.println("Podaj liczbę od 0 do " + amountOfCards);
            playerChoice = scanner.nextInt();
        }
        while (!((playerChoice >= 0 && playerChoice <= amountOfCards) && isChoiceCorrect(playerChoice)));

        if (playerChoice == 0) {
            player.giveCard(gameBoard.getBoardDeck().poll());
        } else {
            Card chosenCard = player.getCards().get(playerChoice - 1);
            gameBoard.putCardOnStack(chosenCard, player);
        }

        gameBoard.checkVictoryStatus(player);
        gameBoard.checkBoardDeckStatus();
    }


    private void computerTurn(int id) {
        Player computer = gameBoard.getPlayers().get(id - 1);
        int amountOfCards = computer.getCards().size();
        Card stackCard = gameBoard.getStack().getLast();
        List<Card> validCards = new ArrayList<>();


        System.out.println("///// Tura Gracza " + computer.getId() + " /////");
        System.out.println("Karta na wierzchu stosu: " + stackCard);
        System.out.println("Gracz " + computer.getId() + " ma " + amountOfCards + " kart");


        for (int i = 0; i < amountOfCards; i++) {
            Card checkedCard = computer.getCards().get(i);
            if (gameBoard.compareCards(checkedCard, stackCard)) {
                validCards.add(checkedCard);
            }
        }
        if (validCards.size() != 0) {
            Collections.shuffle(validCards);
            Card chosenCard = validCards.get(0);

            System.out.println("Gracz " + computer.getId() + " używa " + chosenCard + " karty");
            gameBoard.putCardOnStack(chosenCard, computer);
        } else {
            System.out.println("Gracz " + computer.getId() + " ciągnie nową kartę");
            computer.giveCard(gameBoard.getBoardDeck().poll());
        }

        gameBoard.checkVictoryStatus(computer);
        gameBoard.checkBoardDeckStatus();
    }


    private int decideHowManyPlayers() {
        Scanner scanner = new Scanner(System.in);
        int amountOfPlayers;

        do {
            System.out.println("Podaj ilość graczy (min.2, max.5)");
            amountOfPlayers = scanner.nextInt();
        }
        while (amountOfPlayers < 2 || amountOfPlayers > 5);

        return amountOfPlayers;
    }

    private boolean isChoiceCorrect(int choice) {
        if (choice == 0) {
            return true;
        }

        Card stackCard = gameBoard.getStack().getLast();
        Card chosenCard = gameBoard.getPlayers().get(0).getCards().get(choice - 1);

        if (gameBoard.compareCards(stackCard, chosenCard)) {
            return true;
        } else {
            System.out.println("Nie możesz położyć tej karty! Kolor kart lub stopień musi się zgadzać! Jeżeli nie możesz wyłożyć karty, dobierz kartę! ");
            return false;
        }
    }

    private boolean isVictoryAchieve() {
        for(Player player : gameBoard.getPlayers()){
            if(player.isWinner()){
                System.out.println("Wygrywa Gracz " + player.getId());
                return true;
            }
        }
        return false;
    }


}

