package com.example.task.data.repositories.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.task.domain.models.CommentDomainModel
import com.example.task.domain.repository.CommentRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import retrofit2.HttpException
import java.net.UnknownHostException

class CommentPagingSource @AssistedInject constructor(
    private val commentRepository: CommentRepository,
    @Assisted private val eventId : Long
) : PagingSource<Int, CommentDomainModel>() {
    override fun getRefreshKey(state: PagingState<Int, CommentDomainModel>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CommentDomainModel> {
        return try {
            val page = params.key ?: 0
            val responseList = commentRepository.getComments(page = page, eventId = eventId)
            val nextPageNumber = if (responseList.size < params.loadSize) null else page + 1
            val prevKeyNumber = if (page == 0) null else page - 1
            LoadResult.Page(
                data = responseList,
                prevKey = prevKeyNumber,
                nextKey = nextPageNumber
            )
        } catch (e: HttpException)  {
            LoadResult.Error(e)
        } catch (e : UnknownHostException) {
            LoadResult.Error(e)
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(@Assisted eventId : Long) : CommentPagingSource
    }
}