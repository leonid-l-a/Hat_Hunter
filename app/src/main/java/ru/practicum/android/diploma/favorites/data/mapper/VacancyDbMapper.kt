package ru.practicum.android.diploma.favorites.data.mapper

import ru.practicum.android.diploma.favorites.data.db.FavoritesEntity
import ru.practicum.android.diploma.favorites.data.model.entity.AddressEntity
import ru.practicum.android.diploma.favorites.data.model.entity.ContactsEntity
import ru.practicum.android.diploma.favorites.data.model.entity.EmployerEntity
import ru.practicum.android.diploma.favorites.data.model.entity.EmploymentEntity
import ru.practicum.android.diploma.favorites.data.model.entity.ExperienceEntity
import ru.practicum.android.diploma.favorites.data.model.entity.FilterAreaEntity
import ru.practicum.android.diploma.favorites.data.model.entity.IndustryEntity
import ru.practicum.android.diploma.favorites.data.model.entity.SalaryEntity
import ru.practicum.android.diploma.favorites.data.model.entity.ScheduleEntity
import ru.practicum.android.diploma.favorites.domain.model.VacancyFavorites
import ru.practicum.android.diploma.vacancy.domain.model.VacancyDetail

object VacancyDbMapper {
    fun favoritesEntityToVacancyFavorites(favoritesEntity: FavoritesEntity?): VacancyFavorites? {
        return if (favoritesEntity != null) {
            VacancyFavorites(
                id = favoritesEntity.id,
                name = favoritesEntity.name,
                description = favoritesEntity.description,
                salaryFrom = favoritesEntity.salary?.from,
                salaryTo = favoritesEntity.salary?.to,
                salaryCurrency = favoritesEntity.salary?.currency.orEmpty(),
                addressCity = favoritesEntity.address?.city.orEmpty(),
                addressStreet = favoritesEntity.address?.street.orEmpty(),
                addressBuilding = favoritesEntity.address?.building.orEmpty(),
                fullAddress = favoritesEntity.address?.fullAddress.orEmpty(),
                experienceId = favoritesEntity.experience?.id.orEmpty(),
                experienceName = favoritesEntity.experience?.name.orEmpty(),
                scheduleId = favoritesEntity.schedule?.id.orEmpty(),
                scheduleName = favoritesEntity.schedule?.name.orEmpty(),
                employmentId = favoritesEntity.employment?.id.orEmpty(),
                employmentName = favoritesEntity.employment?.name.orEmpty(),
                contactsId = favoritesEntity.contacts?.id.orEmpty(),
                contactsName = favoritesEntity.contacts?.name.orEmpty(),
                contactsEmail = favoritesEntity.contacts?.email.orEmpty(),
                contactsPhone = favoritesEntity.contacts?.phone.orEmpty(),
                employerId = favoritesEntity.employer?.id.orEmpty(),
                employerName = favoritesEntity.employer?.name.orEmpty(),
                employerLogo = favoritesEntity.employer?.logo.orEmpty(),
                areaId = favoritesEntity.area?.id ?: 0,
                areaName = favoritesEntity.area?.name.orEmpty(),
                areaParentId = favoritesEntity.area?.parentId ?: 0,
                skills = favoritesEntity.skills.orEmpty(),
                url = favoritesEntity.url.orEmpty(),
                industryId = favoritesEntity.industry?.id ?: 0,
                industryName = favoritesEntity.industry?.name.orEmpty()
            )
        } else {
            null
        }
    }

    fun vacancyDetailToFavoritesEntity(vacancyDetail: VacancyDetail): FavoritesEntity {
        return FavoritesEntity(
            id = vacancyDetail.id,
            name = vacancyDetail.name,
            description = vacancyDetail.description,
            salary = SalaryEntity(
                vacancyDetail.salaryFrom,
                vacancyDetail.salaryTo,
                vacancyDetail.salaryCurrency
            ),
            address = AddressEntity(
                vacancyDetail.addressCity,
                vacancyDetail.addressStreet,
                vacancyDetail.addressBuilding,
                vacancyDetail.fullAddress
            ),
            experience = ExperienceEntity(vacancyDetail.experienceId, vacancyDetail.experienceName),
            schedule = ScheduleEntity(vacancyDetail.scheduleId, vacancyDetail.scheduleName),
            employment = EmploymentEntity(vacancyDetail.employmentId, vacancyDetail.employmentName),
            contacts = ContactsEntity(
                vacancyDetail.contactsId,
                vacancyDetail.contactsName,
                vacancyDetail.contactsEmail,
                vacancyDetail.contactsPhone
            ),
            employer = EmployerEntity(
                vacancyDetail.employerId,
                vacancyDetail.employerName,
                vacancyDetail.employerLogo,
            ),
            area = FilterAreaEntity(vacancyDetail.areaId, vacancyDetail.areaName, vacancyDetail.areaParentId),
            skills = vacancyDetail.skills,
            url = vacancyDetail.url,
            industry = IndustryEntity(vacancyDetail.industryId, vacancyDetail.industryName)
        )
    }
}
