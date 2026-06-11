package unicam.cs.hackhub.backend.boundary.dto;

import java.util.List;

public record TeamDTO(
        String nomeTeam,
        List<String> membri
) {}