package dev.soranzo;

public class CommandInfo {
    public String senderName;
    public String senderAvatarUrl;
    public String command;
    public String commandOutput;
    public boolean commandSucess;

    private CommandInfo(Builder builder) {
        this.senderName = builder.senderName;
        this.senderAvatarUrl = builder.senderAvatarUrl;
        this.command = builder.command;
        this.commandOutput = builder.commandOutput;
        this.commandSucess = builder.commandSuccess;
    }

    public static class Builder {
        public String senderName;
        public String senderAvatarUrl;
        public String command;
        public String commandOutput;
        public boolean commandSuccess;

        public Builder senderName(String senderName) {
            this.senderName = senderName;
            return this;
        }

        public Builder senderAvatarUrl(String senderAvatarUrl) {
            this.senderAvatarUrl = senderAvatarUrl;
            return this;
        }

        public Builder command(String command) {
            this.command = command;
            return this;
        }

        public Builder commandOutput(String commandOutput) {
            this.commandOutput = commandOutput;
            return this;
        }

        public Builder commandSuccess(boolean commandSuccess) {
            this.commandSuccess = commandSuccess;
            return this;
        }

        public CommandInfo build() {
            return new CommandInfo(this);
        }
    }
}

