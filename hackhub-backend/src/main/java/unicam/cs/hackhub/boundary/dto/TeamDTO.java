package unicam.cs.hackhub.boundary.dto;

import java.util.List;

public record TeamDTO(
        String nomeTeam,
        List<String> membri
) {}