package dev.soranzo.discord.exceptions;

public class DiscordNotInitializedException extends RuntimeException {
    public DiscordNotInitializedException(String message) {
        super(message);
    }
}
