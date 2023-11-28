package com.digsol.server.stats;

import com.digsol.dto.ViewStatsDto;

import java.util.Collection;

public interface ViewStatsService {
    Collection<ViewStatsDto> getStats(String start, String end, Collection<String> uris, boolean unique);
}
