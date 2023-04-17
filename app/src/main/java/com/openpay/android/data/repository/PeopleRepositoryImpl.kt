package com.openpay.android.data.repository

import com.openpay.android.data.local.dao.PeopleResponseDao
import com.openpay.android.data.remote.api.WebService
import com.openpay.android.model.people.PeopleResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

interface PeopleRepository {
    fun getPeople(): Flow<Resource<PeopleResponse>>
}

@ExperimentalCoroutinesApi
class PeopleRepositoryImpl @Inject constructor(
    private val peopleResponseDao: PeopleResponseDao,
    private val service: WebService
) : PeopleRepository {
    override fun getPeople(): Flow<Resource<PeopleResponse>> {
        return object : NetworkBoundRepository<PeopleResponse, PeopleResponse>() {
            override suspend fun saveRemoteData(response: PeopleResponse) {
                peopleResponseDao.addPeopleResponse(response)
            }

            override fun fetchFromLocal(): Flow<PeopleResponse> {
                return peopleResponseDao.getPeopleResponse()
            }

            override suspend fun fetchFromRemote(): Response<PeopleResponse> {
                return service.getPeople()
            }
        }.asFlow()
    }
}
