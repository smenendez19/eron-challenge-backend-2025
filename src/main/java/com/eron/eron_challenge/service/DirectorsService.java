package com.eron.eron_challenge.service;

import com.eron.eron_challenge.dto.response.DirectorsResponse;

public interface DirectorsService {
    DirectorsResponse fetchData(int threshold);
}