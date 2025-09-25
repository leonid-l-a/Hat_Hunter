package ru.practicum.android.diploma.favorites.data.mapper

import ru.practicum.android.diploma.favorites.domain.model.VacancyFavorites
import ru.practicum.android.diploma.vacancy.domain.model.VacancyDetail

fun VacancyFavorites.toDetail(): VacancyDetail {
    return VacancyDetail(
        id = id,
        name = name,
        description = description,
        salaryFrom = salaryFrom,
        salaryTo = salaryTo,
        salaryCurrency = salaryCurrency,
        addressCity = addressCity,
        addressStreet = addressStreet,
        addressBuilding = addressBuilding,
        fullAddress = fullAddress,
        experienceId = experienceId,
        experienceName = experienceName,
        scheduleId = scheduleId,
        scheduleName = scheduleName,
        employmentId = employmentId,
        employmentName = employmentName,
        contactsId = contactsId,
        contactsName = contactsName,
        contactsEmail = contactsEmail,
        contactsPhone = contactsPhone,
        employerId = employerId,
        employerName = employerName,
        employerLogo = employerLogo,
        areaId = areaId,
        areaName = areaName,
        areaParentId = areaParentId,
        skills = skills,
        url = url,
        industryId = industryId,
        industryName = industryName
    )
}
