package at.biern.scoreboard;

public enum Team {
    ARGENTINA("Argentina"),
    AUSTRALIA("Australia"),
    BELGIUM("Belgium"),
    BRAZIL("Brazil"),
    CAMEROON("Cameroon"),
    CANADA("Canada"),
    CROATIA("Croatia"),
    COSTA_RICA("Costa Rica"),
    DENMARK("Denmark"),
    ECUADOR("Ecuador"),
    ENGLAND("England"),
    FRANCE("France"),
    GERMANY("Germany"),
    GHANA("Ghana"),
    IRAN("Iran"),
    JAPAN("Japan"),
    MEXICO("Mexico"),
    MOROCCO("Morocco"),
    NETHERLANDS("Netherlands"),
    POLAND("Poland"),
    PORTUGAL("Portugal"),
    QATAR("Qatar"),
    SAUDI_ARABIA("Saudi Arabia"),
    SENEGAL("Senegal"),
    SERBIA("Serbia"),
    SOUTH_KOREA("South Korea"),
    SPAIN("Spain"),
    SWITZERLAND("Switzerland"),
    TUNISIA("Tunisia"),
    UNITED_ARAB_EMIRATES("United Arab Emirates"),
    UNITED_STATES("United States"),
    URUGUAY("Uruguay"),
    WALES("Wales");

    private final String displayName;

    Team(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
