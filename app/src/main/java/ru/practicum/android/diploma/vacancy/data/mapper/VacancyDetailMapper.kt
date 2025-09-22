package ru.practicum.android.diploma.vacancy.data.mapper

import ru.practicum.android.diploma.core.data.dto.VacancyDetailDto
import ru.practicum.android.diploma.vacancy.domain.model.VacancyDetail

fun VacancyDetailDto.toDomain(): VacancyDetail {
    return VacancyDetail(
        id = id,
        name = name,
        description = description,
        salaryFrom = salary.from,
        salaryTo = salary.to,
        salaryCurrency = salary.currency,
        addressCity = address.city,
        addressStreet = address.street,
        addressBuilding = address.building,
        fullAddress = address.raw,
        experienceId = experience.id,
        experienceName = experience.name,
        scheduleId = schedule.id,
        scheduleName = schedule.name,
        employmentId = employment.id,
        employmentName = employment.name,
        contactsId = contacts.id,
        contactsName = contacts.name,
        contactsEmail = contacts.email,
        contactsPhone = contacts.phone,
        employerId = employer.id,
        employerName = employer.name,
        employerLogo = employer.logo,
        areaId = area.id,
        areaName = area.name,
        areaParentId = area.parentId,
        skills = skills,
        url = url,
        industryId = industry.id,
        industryName = industry.name
    )
}
